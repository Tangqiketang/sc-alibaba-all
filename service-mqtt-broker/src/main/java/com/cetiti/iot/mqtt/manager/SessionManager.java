package com.cetiti.iot.mqtt.manager;

import com.cetiti.iot.mqtt.bo.ContextBo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 描述:暂时放内存，后面改为存在redis
 *
 * @auther WangMin
 * @create 2022-04-01 16:28
 */
@Slf4j
public class SessionManager {

    //<clientId,上下文详情>
    private static Map<String, ContextBo> clientContextMap = new ConcurrentHashMap<>();
    private static Map<ChannelHandlerContext, ContextBo> ctxMap = new ConcurrentHashMap<>();

    /**
     * 获取所有链接
     * @return
     */
    public static Map<String, ContextBo> get(){
        return clientContextMap;
    }

    public static ContextBo getContextByClientId(String clientId){
        return clientContextMap.get(clientId);
    }

    /**
     * 查询客户端是否在线
     * @param clientId
     * @return
     */
    public static boolean checkClientOnline(String clientId){
        if(clientContextMap.containsKey(clientId)){
            return true;
        }
        return false;
    }

    /**
     *  当有新合法网络链接请求，使用该方法进行网络上线文关系存储
     * */
    public static void addConnect(String clientId, ContextBo contextBo){
        if(StringUtils.isEmpty(clientId) || !verifyContext(contextBo)){
            log.warn("newconnect[{}]的上下文有误{}", contextBo, contextBo);
            return;
        }
        //如果客户端本身带有心跳,覆盖服务端默认的心跳处理
        if (contextBo.getKeepAlive() > 0) {
            if (contextBo.getHandlerContext().pipeline().names().contains("idle")) {
                contextBo.getHandlerContext().pipeline().remove("idle");
            }
            contextBo.getHandlerContext().pipeline().addFirst("idle", new IdleStateHandler(0, 0, Math.round(contextBo.getKeepAlive() * 1.5f)));
        }
        clientContextMap.put(clientId, contextBo);
        ctxMap.put(contextBo.getHandlerContext(), contextBo);
        ClientManager.updateClientOnLine(contextBo.getClientId());

        //发送设备上线消息
        //ProducerBiz.sendActiveMQ(contextBo, true);
    }

    private static boolean verifyContext(ContextBo contextBo){
        if(null == contextBo || null == contextBo.getHandlerContext()){
            return false;
        }
        return true;
    }

    /**
     * 移除连接
     * */
    public static void removeContextByCtx(ChannelHandlerContext ctx){
        try{
            if(null == ctx || !ctxMap.containsKey(ctx) ){
                return ;
            }
            ctx.close();
            ContextBo contextBo = ctxMap.get(ctx);
            if(!verifyContext(contextBo)){
                log.warn("disconnect[{}]的上下文有误", contextBo);
                return;
            }
            clientContextMap.remove(contextBo.getClientId());
            ctxMap.remove(contextBo.getHandlerContext());
            ClientManager.removeClient(contextBo.getClientId());
            //发送设备下线通知
            //ProducerBiz.sendActiveMQ(contextBo, false);
        }catch (Exception e){

        }
    }

    /**
     * 移除连接
     * */
    public static void removeContextByClientId(String clientId){
        try{
            if(StringUtils.isEmpty(clientId) || !clientContextMap.containsKey(clientId) ){
                return ;
            }

            ContextBo contextBo = clientContextMap.get(clientId);
            if(!verifyContext(contextBo)){
                log.warn("disconnect[{}]的上下文有误{}", contextBo, contextBo);
                return;
            }
            contextBo.getHandlerContext().close();
            clientContextMap.remove(clientId);
            ctxMap.remove(contextBo.getHandlerContext());
            //发送设备下线通知
            //ProducerBiz.sendActiveMQ(contextBo, false);
        }catch (Exception e){

        }
    }




}

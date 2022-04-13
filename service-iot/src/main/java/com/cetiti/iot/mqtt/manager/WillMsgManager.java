package com.cetiti.iot.mqtt.manager;

import com.cetiti.iot.mqtt.bo.WillMsgBo;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-04-02 9:25
 */
@Slf4j
public class WillMsgManager {

    /**
     * <clientId,WillMsgBo>
     */
    private static Map<String, WillMsgBo> willMap = new HashMap();

    /**
     * 添加到遗嘱列表
     * @param msg
     */
    public static void addWill(WillMsgBo msg){
        if(null == msg){
            return;
        }
        willMap.put(msg.getClientId(), msg);
    }


    public static void sendWill(String clientId){
        try{
            WillMsgBo willMsgBo = willMap.get(clientId);
            if(null == willMsgBo){
                return;
            }
            // SendManager.sendMessage();
           ClientManager.pubTopic(willMsgBo.getMsg());
        }catch (Exception e){
            log.warn("发送客户端[{}]的遗嘱消息异常", e);
        }
    }

}

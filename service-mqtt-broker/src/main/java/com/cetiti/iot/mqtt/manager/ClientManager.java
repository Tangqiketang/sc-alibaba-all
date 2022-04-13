package com.cetiti.iot.mqtt.manager;

import com.alibaba.fastjson.JSON;
import com.cetiti.iot.mqtt.bo.ContextBo;
import com.cetiti.iot.mqtt.config.MqttConstants;
import com.cetiti.com.cetiti.iot.mqtt.util.ArrayUtil;
import com.cetiti.com.cetiti.iot.mqtt.util.CombinationUtil;
import com.cetiti.iot.mqtt.manager.SessionManager;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 描述:保存客户端信息,后面改为redis
 *
 * @auther WangMin
 * @create 2022-04-01 17:16
 */
@Slf4j
public class ClientManager {

    /**
     * 在线状态
     * <clientId,time>
     */
    private static Map<String, Long> clientStatusMap = new ConcurrentHashMap<>();

    /**
     * <clientId,<topic,true>>
     * */
    private static Map<String, Map<String, Boolean>> clientTopicMap = new ConcurrentHashMap<>();


    /**
     * <topic,<clientId,Session>>
     * value不适用map<ContextBo, String>的原因是，订阅时可能Qos改变，所以每次订阅都需要重新设置
     */
    private static Map<String, Map<String, ContextBo>> topicMap = new ConcurrentHashMap<>();



    /**
     * 更新设备在线
     * */
    public static void updateClientOnLine(String clientId){
        clientStatusMap.put(clientId, System.currentTimeMillis());
    }


    /**
     * 存入topic-<clientID-session>关系
     * 存入<client-topic>关系
     */
    public static void addClient(String topic, ContextBo contextBo) {
        try {
            Map<String, ContextBo> clientMap = topicMap.get(topic);
            if (CollectionUtils.isEmpty(clientMap)) {
                clientMap = new HashMap<>();
            }
            clientMap.put(contextBo.getClientId(), contextBo);
            topicMap.put(topic, clientMap);

            /**开始处理client对应所有topic**/
            Map<String, Boolean> topics = null;
            if(clientTopicMap.containsKey(contextBo.getClientId())){
                topics = clientTopicMap.get(contextBo.getClientId());
                if(!topics.containsKey(topic)){
                    topics.put(topic, true);
                }
            }else{
                topics = new HashMap<>();
                topics.put(topic, true);
                clientTopicMap.put(contextBo.getClientId(), topics);
            }
        } catch (Exception e) {
            log.warn("异常", e);
        }
    }

    /**
     * 移除client
     * */
    public static void removeClient(String clientId){
        try{
            Map<String, Boolean> topics = clientTopicMap.get(clientId);
            if(null != topics){
                for(String key : topics.keySet()){
                    Map<String, ContextBo> clientMap = topicMap.get(key);
                    if(CollectionUtils.isEmpty(clientMap)){
                        continue;
                    }
                    clientMap.remove(clientId);
                }
                clientTopicMap.remove(clientId);
            }
            clientStatusMap.remove(clientId);
        }catch (Exception e){
            log.warn("移除client[{}]异常", e);
        }
    }


    /**
     * 将消息发送到指定topic下的所有client上去
     */
    public static void pubTopic(MqttPublishMessage msg) {
        String topic = msg.variableHeader().topicName();
        List<String> topicList = searchTopic(topic);
        for(String itemTopic:topicList){
            Map<String, ContextBo> clientMap = topicMap.get(itemTopic);
            if(CollectionUtils.isEmpty(clientMap)){
                continue;
            }
            for(ContextBo contextBo:clientMap.values()){
                if(!checkClientValid(contextBo.getClientId())){
                    log.warn("{}不在线", contextBo.getClientId());
                    continue;
                }
                //往每个client发送
                SenderManager.pubMsg(msg, contextBo);
            }
        }

    }

    /**
     *根据现有topic获取所有符合通配符的topic
     * 后面做优化缓存 TODO
     * */
    private static List<String> searchTopic(String topic){
        try{
            List<String> topicList = new ArrayList<>();
            /**先把自己加进去*/
            topicList.add(topic);
            /**先处理#通配符*/
            String[] filterDup = topic.split("/");
            int[] src = new int[filterDup.length];
            String itemTopic = "";
            for(int i =0 ; i < filterDup.length; i++){
                String item = itemTopic.concat("#");
                topicList.add(item);
                itemTopic = itemTopic.concat(filterDup[i]).concat("/");
                src[i] = i;
                continue;
            }

            /**处理+通配符*/
            Map<List<Integer>, Boolean> map = CombinationUtil.alg(src);
            for(List<Integer> key:map.keySet()){
                String[] arr = ArrayUtil.copyString(filterDup);
                for(Integer index:key){
                    arr[index] = "+";
                }
                String newTopic = ArrayUtil.concat(arr, "/");
                topicList.add(newTopic);
            }

            return topicList;

        }catch (Exception e){
            log.warn("", e);
            return null;
        }
    }

    /**
     * 检查客户端是否在线超时,用于ping
     * */
    public static Boolean checkClientValid(String clientId){
        long currTime = System.currentTimeMillis();
        Long timestamp = clientStatusMap.get(clientId);
        if(null == timestamp){
            return false;
        }
        if(currTime - timestamp > MqttConstants.DEVICE_PING_EXPIRED){
            clientStatusMap.remove(clientId);
            SessionManager.removeContextByClientId(clientId);
            return false;
        }
        return true;
    }

    /**
     * 客户端取消订阅
     * 删除指定topic下的指定client，取消订阅的时候用
     * */
    public static void unsubscribe(String topic, ContextBo contextBo) {
        try {
            Map<String, ContextBo> clientMap = topicMap.get(topic);
            if (CollectionUtils.isEmpty(clientMap)) {
                return;
            }
            ContextBo item = clientMap.get(contextBo.getClientId());
            if(null == item){
                return;
            }
            clientMap.remove(contextBo.getClientId());
        } catch (Exception e) {
            log.warn("异常", e);
        }
    }


    public static void main(String[] args) {
        String topic = "/aa/bb/cc";
        System.out.println(JSON.toJSONString(searchTopic(topic)));
    }
}

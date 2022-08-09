package com.cetiti.iot.mqtt.handler.standHandler;

import com.cetiti.iot.mqtt.bo.ContextBo;
import com.cetiti.iot.mqtt.bo.RetainMsgBo;
import com.cetiti.iot.mqtt.manager.ClientManager;
import com.cetiti.iot.mqtt.manager.RetainMsgManager;
import com.cetiti.iot.mqtt.manager.SenderManager;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.mqtt.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-04-06 16:00
 */
@Component
@Slf4j
public class SubscribeHandler {
    public void onSubscribe(ContextBo contextBo, MqttSubscribeMessage msg) {
        log.debug("onSubscribe-handler请求:{}", msg);

        List<MqttTopicSubscription> topicList = msg.payload().topicSubscriptions();
        if(!validTopicFilter(topicList)){
            log.warn("订阅的主题非法{}", topicList);
            //TODO 这个需要返回一个错误码
            return;
        }
        //服务端订阅确认 SUBSCRIBE-SUBACK
        MqttSubAckPayload mqttSubAckPayload = new MqttSubAckPayload(MqttQoS.AT_MOST_ONCE.value());
        MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(
                MqttMessageType.SUBACK,
                false,
                MqttQoS.AT_MOST_ONCE,
                false,
                0
        );
        MqttMessageIdVariableHeader mqttMessageIdVariableHeader = MqttMessageIdVariableHeader.from(msg.variableHeader().messageId());
        MqttSubAckMessage mqttSubAckMessage = (MqttSubAckMessage)MqttMessageFactory.newMessage(
                mqttFixedHeader, mqttMessageIdVariableHeader, mqttSubAckPayload);
        SenderManager.responseMsg(contextBo, mqttSubAckMessage, null, true);

        //新增topic对应session关系
        for(MqttTopicSubscription item:topicList){
            ClientManager.addClient(item.topicName(), contextBo);
        }
        //更新客户端在线时间
        ClientManager.updateClientOnLine(contextBo.getClientId());

        //客户端请求订阅后立刻会收到的一条消息retainMsg.希望sub方订阅成功后立马执行的控制命令
        for(MqttTopicSubscription item:topicList){
            this.sendRetain(item.topicName(),contextBo, item.qualityOfService());
        }

    }

    private void sendRetain(String topic, ContextBo contextBo, MqttQoS mqttQoS) {
        try {
            RetainMsgBo retainMsg = RetainMsgManager.latestRetain(topic);
            if(null == retainMsg){
                return;
            }
            //如果之前存的遗嘱等级比较
            MqttQoS respQoS = retainMsg.getMqttQoS() > mqttQoS.value() ? mqttQoS : MqttQoS.valueOf(retainMsg.getMqttQoS());
            if (respQoS == MqttQoS.AT_MOST_ONCE) {
                MqttPublishMessage publishMessage = (MqttPublishMessage) MqttMessageFactory.newMessage(
                        new MqttFixedHeader(MqttMessageType.PUBLISH, false, respQoS, false, 0),
                        new MqttPublishVariableHeader(retainMsg.getTopic(), 0), Unpooled.buffer().writeBytes(retainMsg.getMessageBytes()));

                SenderManager.pubMsg(publishMessage, contextBo);
            }
            if (respQoS == MqttQoS.AT_LEAST_ONCE) {
                //TODO 需要生成一个独一无二的ID
                int messageId = 0;
                MqttPublishMessage publishMessage = (MqttPublishMessage) MqttMessageFactory.newMessage(
                        new MqttFixedHeader(MqttMessageType.PUBLISH, false, respQoS, false, 0),
                        new MqttPublishVariableHeader(retainMsg.getTopic(), messageId), Unpooled.buffer().writeBytes(retainMsg.getMessageBytes()));
                SenderManager.pubMsg(publishMessage, contextBo);
            }
            if (respQoS == MqttQoS.EXACTLY_ONCE) {
                //TODO 需要生成一个独一无二的ID
                int messageId = 0;
                MqttPublishMessage publishMessage = (MqttPublishMessage) MqttMessageFactory.newMessage(
                        new MqttFixedHeader(MqttMessageType.PUBLISH, false, respQoS, false, 0),
                        new MqttPublishVariableHeader(retainMsg.getTopic(), messageId), Unpooled.buffer().writeBytes(retainMsg.getMessageBytes()));
                SenderManager.pubMsg(publishMessage, contextBo);
            }
        }catch (Exception e){
            log.warn("订阅消息时发遗留消息异常:{}", contextBo, e);
        }

    }


    /**检查topic合法性*/
    private boolean validTopicFilter(List<MqttTopicSubscription> topicSubscriptions) {
        for (MqttTopicSubscription topicSubscription : topicSubscriptions) {
            String topicFilter = topicSubscription.topicName();
            if(StringUtils.isEmpty(topicFilter)){
                return false;
            }
        }
        return true;
    }
}

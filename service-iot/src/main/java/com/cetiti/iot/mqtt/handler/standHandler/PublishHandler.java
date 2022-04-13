package com.cetiti.iot.mqtt.handler.standHandler;

import com.cetiti.iot.mqtt.bo.ContextBo;
import com.cetiti.iot.mqtt.bo.msg.DeviceUpMessageBo;
import com.cetiti.iot.mqtt.config.BrokerProperties;
import com.cetiti.iot.mqtt.manager.ClientManager;
import com.cetiti.iot.mqtt.manager.RetainMsgManager;
import com.cetiti.iot.mqtt.manager.SenderManager;
import io.netty.buffer.ByteBufUtil;
import io.netty.handler.codec.mqtt.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 描述:
 * \
 *
 * @auther WangMin
 * @create 2022-04-06 15:57
 */
@Component
@Slf4j
public class PublishHandler {

    @Autowired
    private BrokerProperties brokerProperties;

    public void onPublish(ContextBo contextBo, MqttPublishMessage msg) {
        log.info("onPublish-handler请求收到:{}",msg);
        this.retainMsg(msg);
        this.pubAck(contextBo,msg);
        //透传消息到所有订阅该主题的客户端
        this.sendMsgToClient(msg);

        this.sendMq(msg,contextBo);
    }

    private void sendMq(MqttPublishMessage msg,ContextBo contextBo){
        String topic = msg.variableHeader().topicName();
        if(false){
            /**只处理设备上传的topic* TODO**/
            return;
        }
        DeviceUpMessageBo deviceUpMessageBo = new DeviceUpMessageBo();
        deviceUpMessageBo.setClientId(contextBo.getClientId());
        deviceUpMessageBo.setTopic(topic);
        deviceUpMessageBo.setPacketId((long)msg.variableHeader().packetId());
        deviceUpMessageBo.setSourceMsg(ByteBufUtil.getBytes(msg.content()));
        deviceUpMessageBo.setBrokerId(brokerProperties.getId());

        //不同的设备发送到不同的队列
        this.dispatchTopic2DiffMq(deviceUpMessageBo);


    }


    /**先给发送的人反馈下收到了*/
    private void pubAck(ContextBo contextBo, MqttPublishMessage msg){
        MqttQoS mqttQoS = msg.fixedHeader().qosLevel();
        MqttFixedHeader fixedHeader = null;
        //Qos2需要二阶段
        if (mqttQoS.value() <= 1) {
            fixedHeader = new MqttFixedHeader(MqttMessageType.PUBACK, false, mqttQoS, false, 0);
        } else {
            fixedHeader = new MqttFixedHeader(MqttMessageType.PUBREC, false, MqttQoS.AT_MOST_ONCE, false, 0);
        }
        String topicName = msg.variableHeader().topicName();
        int msgId = msg.variableHeader().packetId();
        MqttMessageIdVariableHeader msgIdVariableHeader = null;
        if(msgId > 0){
            msgIdVariableHeader = MqttMessageIdVariableHeader.from(msgId);
        }
        MqttPubAckMessage ackMessage = new MqttPubAckMessage(fixedHeader, msgIdVariableHeader);
        if(mqttQoS.value() < 1){
            //无需确认收到
        }else{
            SenderManager.responseMsg(contextBo, ackMessage, msgId, true);
        }

    }

    /**
     * 将publish接收到的消息主动推送到该topic下所有终端上去
     * */
    private void sendMsgToClient(MqttPublishMessage msg){
        try{
            ClientManager.pubTopic(msg);
        }catch (Exception e){
            log.warn("发消息异常:{}",e);
        }
    }



    private void retainMsg( MqttPublishMessage msg){
        try{
            RetainMsgManager.pushRetain(msg);
        }catch (Exception e){
            log.warn("遗留消息处理异常",msg, e);
        }
    }

    private void dispatchTopic2DiffMq(DeviceUpMessageBo deviceUpMessageBo) {



    }
}

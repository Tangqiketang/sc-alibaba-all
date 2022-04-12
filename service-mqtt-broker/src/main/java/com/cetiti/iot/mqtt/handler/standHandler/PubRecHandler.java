package com.cetiti.iot.mqtt.handler.standHandler;

import com.cetiti.iot.mqtt.bo.ContextBo;
import com.cetiti.iot.mqtt.manager.SenderManager;
import io.netty.handler.codec.mqtt.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-04-06 15:59
 */
@Component
@Slf4j
public class PubRecHandler {
    public void onPubRec(ContextBo contextBo, MqttMessage msg) {
        int msgId = ((MqttMessageIdVariableHeader)msg.variableHeader()).messageId();
        MqttMessageIdVariableHeader msgIdVariableHeader = MqttMessageIdVariableHeader.from(msgId);

        MqttMessage relMessage = MqttMessageFactory.newMessage(
                new MqttFixedHeader(MqttMessageType.PUBREL,false, MqttQoS.AT_LEAST_ONCE,false,2)
                ,msgIdVariableHeader
                ,null);

        SenderManager.responseMsg(contextBo,relMessage,msgId,true);
        log.debug("onPubRec-handler请求发送PUBREL消息:{}", relMessage);
    }
}

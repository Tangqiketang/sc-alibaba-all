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
 * @create 2022-04-06 16:00
 */
@Component
@Slf4j
public class PubRelHandler {

    public void onPubRel(ContextBo contextBo, MqttMessage msg) {
        int msgId = ((MqttMessageIdVariableHeader)msg.variableHeader()).messageId();
        MqttMessageIdVariableHeader msgIdVariableHeader = MqttMessageIdVariableHeader.from(msgId);
        //???? at_most_once todo
        MqttMessage compMsg = MqttMessageFactory.newMessage(
                new MqttFixedHeader(MqttMessageType.PUBCOMP, false, MqttQoS.AT_MOST_ONCE, false, 2),
                msgIdVariableHeader,
                null);

        SenderManager.responseMsg(contextBo, compMsg, msgId, true);
        log.debug("onPubRel-handler请求发送PUBCOMP消息:{}", compMsg);
    }
}

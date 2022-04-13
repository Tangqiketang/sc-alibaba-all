package com.cetiti.iot.mqtt.handler.standHandler;

import com.cetiti.iot.mqtt.bo.ContextBo;
import com.cetiti.iot.mqtt.manager.ClientManager;
import com.cetiti.iot.mqtt.manager.SenderManager;
import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttQoS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-04-06 15:49
 */
@Component
@Slf4j
public class PingReqHandler {


    public void onPingReq(ContextBo contextBo) {
        try{
            log.debug("收到ping {}", contextBo);
            MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.PINGRESP, false, MqttQoS.AT_MOST_ONCE, false, 0);
            MqttMessage mqttMessage = new MqttMessage(mqttFixedHeader);
            ClientManager.updateClientOnLine(contextBo.getClientId());
            //发送设备上线消息
            //ProducerB.sendActiveMQ(contextBo, true);
            SenderManager.sendMessage(mqttMessage, contextBo.getClientId(), null, true);
        }catch (Exception e){
            log.warn("处理ping消息异常:{}", e);
        }
    }
}

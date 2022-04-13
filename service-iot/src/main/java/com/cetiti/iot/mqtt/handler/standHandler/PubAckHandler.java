package com.cetiti.iot.mqtt.handler.standHandler;

import com.cetiti.iot.mqtt.bo.ContextBo;
import io.netty.handler.codec.mqtt.MqttPubAckMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-04-06 15:50
 */
@Component
@Slf4j
public class PubAckHandler {

    public void onPubAck(ContextBo contextBo, MqttPubAckMessage msg) {
        //QoS1基本的客户端响应，至少一次。不用做任何处理
    }
}

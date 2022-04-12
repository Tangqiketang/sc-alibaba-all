package com.cetiti.iot.mqtt.handler.standHandler;

import com.cetiti.iot.mqtt.bo.ContextBo;
import io.netty.handler.codec.mqtt.MqttMessage;
import org.springframework.stereotype.Component;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-04-06 15:56
 */
@Component
public class PubCompHandler {
    public void onPubComp(ContextBo contextBo, MqttMessage msg) {
        //完成不需要返回
    }
}

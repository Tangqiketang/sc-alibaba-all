package com.cetiti.iot.mqtt.handler.standHandler;

import com.cetiti.iot.mqtt.bo.ContextBo;
import io.netty.handler.codec.mqtt.MqttUnsubscribeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-04-06 16:01
 */
@Component
@Slf4j
public class UnSubscribeHandler{

    public void onUnsubscribe(ContextBo contextBo, MqttUnsubscribeMessage msg) {
        log.info("onSubscribe-handler请求:{}",msg);
    }
}

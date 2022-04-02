package com.cetiti.iot.mqtt.bo;

import io.netty.handler.codec.mqtt.MqttPublishMessage;
import lombok.Data;

import java.io.Serializable;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-04-02 9:27
 */
@Data
public class WillMsgBo implements Serializable {

    private static final long serialVersionUID = -6054051345590588841L;
    private String clientId;
    private boolean cleanSession;
    private String topic;

    //netty自带的消息类
    private MqttPublishMessage msg;

    public WillMsgBo(String clientId, String topic, boolean cleanSession, MqttPublishMessage willMessage) {
        this.clientId = clientId;
        this.cleanSession = cleanSession;
        this.msg = willMessage;
        this.topic = topic;
    }

}

package com.cetiti.iot.mqtt.bo;

import java.io.Serializable;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-04-07 16:42
 */
public class RetainMsgBo implements Serializable {
    private static final long serialVersionUID = -8632127316386582243L;

    private String topic;
    private byte[] messageBytes;
    private int mqttQoS;


    public String getTopic() {
        return topic;
    }

    public RetainMsgBo setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public byte[] getMessageBytes() {
        return messageBytes;
    }

    public RetainMsgBo setMessageBytes(byte[] messageBytes) {
        this.messageBytes = messageBytes;
        return this;
    }

    public int getMqttQoS() {
        return mqttQoS;
    }

    public RetainMsgBo setMqttQoS(int mqttQoS) {
        this.mqttQoS = mqttQoS;
        return this;
    }
}

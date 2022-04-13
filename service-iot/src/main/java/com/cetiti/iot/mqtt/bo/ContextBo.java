package com.cetiti.iot.mqtt.bo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttVersion;
import lombok.Data;

/**
 * 描述: 会话详情
 *
 * @auther WangMin
 * @create 2022-04-01 16:31
 */
@Data
public class ContextBo {

    private int keepAlive;
    private String clientId;

    private Boolean cleanSession = false;
    private int keepAliveMax;
    private String userName;    //deviceName&productKey
    private Boolean connected = false;
    private MqttVersion version;
    //会话
    ChannelHandlerContext handlerContext;
    //?
    private MqttPublishMessage willMessage;


}

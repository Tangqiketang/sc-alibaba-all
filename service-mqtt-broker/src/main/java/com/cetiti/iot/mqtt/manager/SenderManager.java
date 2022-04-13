package com.cetiti.iot.mqtt.manager;

import com.cetiti.iot.mqtt.bo.ContextBo;
import com.cetiti.iot.mqtt.manager.SessionManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.mqtt.*;
import lombok.extern.slf4j.Slf4j;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-04-02 14:10
 */
@Slf4j
public class SenderManager {

    /**
     * 发送各种类型的MqttMessage信息
     *用于服务端向客户端下发消息，通过clientID 找到当时的channel
     */
    public static void sendMessage(MqttMessage msg, String clientId, Integer packetId, boolean flush) {
        ContextBo contextBo = SessionManager.getContextByClientId(clientId);
        if (contextBo == null || null == contextBo.getHandlerContext()) {
            String pid = packetId == null || packetId <= 0 ? "" : String.valueOf(packetId);
            return;
        }
        responseMsg(contextBo, msg, packetId, flush);
    }


    /**
     * 发送各种类型的MqttMessage信息
     *
     */
    public static void responseMsg(ContextBo contextBo, MqttMessage msg, Integer packetId, boolean flush) {
        String pid = packetId == null || packetId <= 0 ? "" : String.valueOf(packetId);
        ChannelFuture future = flush ? contextBo.getHandlerContext().writeAndFlush(msg) : contextBo.getHandlerContext().write(msg);
        future.addListener(f -> {
            if (f.isSuccess()) {
                log.debug("成功向[{}]发送消息:{}", contextBo.getClientId(), msg);
            } else {
                log.debug("失败向[{}]发送消息:{}  失败原因:{}", contextBo.getClientId(), msg, f.cause());
            }
        });
    }

    /**
     * 向单个client发送指定类型MqttPublishMessage消息
     *
     */
    public static void pubMsg(MqttPublishMessage msg, ContextBo contextBo) {

        try {
            final Channel channel = contextBo.getHandlerContext().channel();
            MqttQoS qos = msg.fixedHeader().qosLevel();
            int dupTimes = 0;
            ByteBuf sendBuf = msg.content().retainedDuplicate();
            sendBuf.resetReaderIndex();
            MqttFixedHeader Header = new MqttFixedHeader(MqttMessageType.PUBLISH,
                    dupTimes > 0, qos, msg.fixedHeader().isRetain(), 0);
            MqttPublishVariableHeader publishVariableHeader = new MqttPublishVariableHeader(
                    msg.variableHeader().topicName(), msg.variableHeader().packetId());
            MqttPublishMessage publishMessage = new MqttPublishMessage(Header,
                    publishVariableHeader, sendBuf);
            channel.writeAndFlush(publishMessage);

        } catch (Exception e) {
            log.warn("发送消息异常 {}", msg, e);
        }
    }

}

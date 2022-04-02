package com.cetiti.iot.mqtt.handler;

import com.cetiti.iot.mqtt.manager.SessionManager;
import com.cetiti.iot.mqtt.manager.WillMsgManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;

/**
 * 描述:
 * mqtt消息接收处理类
 *
 * @auther WangMin
 * @create 2022-04-01 15:58
 */
public class MqttReceiveHandler extends SimpleChannelInboundHandler<MqttMessage> {

    private HandlerFatory handlerFatory;

    public MqttReceiveHandler(HandlerFatory handlerFatory){
        this.handlerFatory = handlerFatory;
    }


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MqttMessage mqttMessage) throws Exception {

    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
            throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.ALL_IDLE) {
                Channel channel = ctx.channel();
                String clientId = (String) channel.attr(AttributeKey.valueOf("clientId")).get();
                // 超时发送遗嘱消息
                WillMsgManager.sendWill(clientId);
                ctx.close();
                SessionManager.removeContextByCtx(ctx);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}

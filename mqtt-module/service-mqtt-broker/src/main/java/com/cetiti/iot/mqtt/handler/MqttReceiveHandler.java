package com.cetiti.iot.mqtt.handler;

import com.cetiti.iot.mqtt.bo.ContextBo;
import com.cetiti.iot.mqtt.manager.SenderManager;
import com.cetiti.iot.mqtt.manager.SessionManager;
import com.cetiti.iot.mqtt.manager.WillMsgManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.mqtt.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 描述:
 * mqtt消息接收处理类
 *
 * @auther WangMin
 * @create 2022-04-01 15:58
 */
@Slf4j
public class MqttReceiveHandler extends SimpleChannelInboundHandler<MqttMessage> {
    private ContextBo contextBo = new ContextBo();

    private HandlerFactory handlerFactory;

    public MqttReceiveHandler(HandlerFactory handlerFactory){
        this.handlerFactory = handlerFactory;
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MqttMessage msg) throws Exception {
        try {
            log.debug("channelRead0收到消息:{}", msg);
            this.contextBo.setHandlerContext(ctx);
            this.verifySysDecodeResult(ctx, msg);

            this.dispatch(ctx,msg);
        }catch (Exception e){
            ReferenceCountUtil.release(msg);
            log.warn("处理消息异常{}",msg, e);
        }
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

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.warn("发生了错误,此连接被关闭", cause.getMessage());
        SessionManager.removeContextByCtx(ctx);
    }

    private void verifySysDecodeResult(ChannelHandlerContext ctx, MqttMessage msg){
        if (msg.decoderResult().isFailure()) {
            Throwable cause = msg.decoderResult().cause();
            if (cause instanceof MqttUnacceptableProtocolVersionException) {
                //协议版本异常
                SenderManager.responseMsg(
                        contextBo,
                        MqttMessageFactory.newMessage(
                                new MqttFixedHeader(MqttMessageType.CONNACK, false, MqttQoS.AT_MOST_ONCE, false, 0),
                                new MqttConnAckVariableHeader(MqttConnectReturnCode.CONNECTION_REFUSED_UNACCEPTABLE_PROTOCOL_VERSION, false),
                                null),
                        null,
                        true);
            } else if (cause instanceof MqttIdentifierRejectedException) {
                SenderManager.responseMsg(
                        contextBo,
                        MqttMessageFactory.newMessage(
                                new MqttFixedHeader(MqttMessageType.CONNACK, false, MqttQoS.AT_MOST_ONCE, false, 0),
                                new MqttConnAckVariableHeader(MqttConnectReturnCode.CONNECTION_REFUSED_IDENTIFIER_REJECTED, false),
                                null),
                        null,
                        true);
            }
            SessionManager.removeContextByCtx(ctx);
            return;
        }
    }

    /**
     * QoS1:
     *      1.Sender->publish->receiver
     *      2.Sender<-pubAck<-receiver
     * QoS2:
     *      1.Sender->publish->receiver
     *      2.Sender<-pubRec<-receiver
     *      3.Sender->pubRel->receiver
     *      4.Sender<-pubComp<-receiver
     * https://blog.csdn.net/solo_jm/article/details/103402309
     *
     * @param ctx
     * @param msg
     */
    private void dispatch(ChannelHandlerContext ctx, MqttMessage msg){
        switch (msg.fixedHeader().messageType()) {
            //客户端发起连接
            case CONNECT:
                this.handlerFactory.connectHandler.onConnect(this.contextBo, (MqttConnectMessage) msg);
                break;
            //客户端断开连接请求
            case DISCONNECT:
                this.handlerFactory.disconnectHandler.onDisconnect(contextBo);
                break;
            //客户端发送心跳
            case PINGREQ:
                this.handlerFactory.pingReqHandler.onPingReq(contextBo);
                break;
            //客户端发布普通消息
            case PUBLISH:
                this.handlerFactory.publishHandler.onPublish(this.contextBo, (MqttPublishMessage) msg);
                break;
            //客户端对服务端发布QoS1内容的确认 (QoS 1等级的PUBLISH报文的响应)
            case PUBACK:
                this.handlerFactory.pubAckHandler.onPubAck(this.contextBo, (MqttPubAckMessage)msg);
                break;
            //服务端发布QoS2内容，客户端确认收到packageId(QoS2第一阶段)
            case PUBREC:
                this.handlerFactory.pubRecHandler.onPubRec(this.contextBo, msg);
                break;
            //客户端发布内容，确认服务端接收到后，通知服务端可以删除Rec包的id(QoS2第二阶段)
            case PUBREL:
                this.handlerFactory.pubRelHandler.onPubRel(this.contextBo, msg);
                break;
            //服务端发布QoS2内容,客户端确认完成(QoS2第三阶段)
            case PUBCOMP:
                this.handlerFactory.pubCompHandler.onPubComp(this.contextBo, msg);
                break;
            //请求订阅
            case SUBSCRIBE:
                this.handlerFactory.subscribeHandler.onSubscribe(contextBo, (MqttSubscribeMessage) msg);
                break;
            //取消订阅
            case UNSUBSCRIBE:
                this.handlerFactory.unSubscribeHandler.onUnsubscribe(contextBo, (MqttUnsubscribeMessage) msg);
                break;
        }

    }

}

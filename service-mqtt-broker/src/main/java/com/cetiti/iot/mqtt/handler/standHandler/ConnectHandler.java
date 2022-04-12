package com.cetiti.iot.mqtt.handler.standHandler;

import com.cetiti.iot.mqtt.bo.ContextBo;
import com.cetiti.iot.mqtt.bo.WillMsgBo;
import com.cetiti.iot.mqtt.manager.SenderManager;
import com.cetiti.iot.mqtt.manager.SessionManager;
import com.cetiti.iot.mqtt.manager.WillMsgManager;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 描述:MQTT连接
 *
 * @auther WangMin
 * @create 2022-04-06 14:40
 */
@Component
@Slf4j
public class ConnectHandler {


    public void onConnect(ContextBo contextBo, MqttConnectMessage msg) {
        log.info("onConnect-handler请求:{}", msg);
        ChannelHandlerContext ctx = contextBo.getHandlerContext();

        MqttVersion version = MqttVersion.fromProtocolNameAndLevel(msg.variableHeader().name(), (byte) msg.variableHeader().version());
        String clientId = msg.payload().clientIdentifier();
        String userName = msg.payload().userName();
        boolean cleanSession = msg.variableHeader().isCleanSession();
        contextBo.setVersion(version);
        contextBo.setClientId(clientId);
        contextBo.setUserName(userName);
        contextBo.setCleanSession(cleanSession);

        if (msg.variableHeader().keepAliveTimeSeconds() > 0 && msg.variableHeader().keepAliveTimeSeconds() <= contextBo.getKeepAliveMax()) {
            int keepAlive = msg.variableHeader().keepAliveTimeSeconds();
            contextBo.setKeepAlive(keepAlive);
        }
        
        if(!authCheck(contextBo, msg)){
            log.warn("用户名密码错误:{}", msg);
            contextBo.getHandlerContext().close();
            return;
        }

        SessionManager.removeContextByClientId(contextBo.getClientId());
        SessionManager.addConnect(contextBo.getClientId(), contextBo);
        contextBo.setConnected(true);
        //增加一个遗嘱信息
        addWillMsg(msg);
        //返回成功
        SenderManager.responseMsg(
                contextBo,
                MqttMessageFactory.newMessage(
                        new MqttFixedHeader(MqttMessageType.CONNACK, false, MqttQoS.AT_MOST_ONCE, false, 0x02),
                        new MqttConnAckVariableHeader(MqttConnectReturnCode.CONNECTION_ACCEPTED, !contextBo.getCleanSession()),
                        null),
                null,
                true);


    }

    private boolean authCheck(ContextBo contextBo, MqttConnectMessage msg) {
        //TODO 三元组认证
        return true;
    }

    private void addWillMsg(MqttConnectMessage msg){
        if (!msg.variableHeader().isWillFlag()) {
            return;
        }
        log.info("connecthandler.processWillMsg+++");
        MqttPublishMessage willMessage = (MqttPublishMessage) MqttMessageFactory.newMessage(
                new MqttFixedHeader(MqttMessageType.PUBLISH, false,
                        MqttQoS.valueOf(msg.variableHeader().willQos()),
                        msg.variableHeader().isWillRetain(), 0),
                new MqttPublishVariableHeader(msg.payload().willTopic(), 0),
                Unpooled.buffer().writeBytes(msg.payload().willMessageInBytes()));

        WillMsgBo willMsgBo = new WillMsgBo(msg.payload().clientIdentifier(),
                msg.payload().willTopic(), msg.variableHeader().isCleanSession(), willMessage);
        WillMsgManager.addWill(willMsgBo);
    }
}

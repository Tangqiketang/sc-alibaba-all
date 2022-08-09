package com.cetiti.iot.mqtt.handler;

import com.cetiti.iot.mqtt.handler.standHandler.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-04-01 16:02
 */
@Component
public class HandlerFactory {

    @Resource
    ConnectHandler connectHandler;

    @Resource
    DisconnectHandler disconnectHandler;

    @Resource
    PingReqHandler pingReqHandler;

    @Resource
    PubAckHandler pubAckHandler;

    @Resource
    PubCompHandler pubCompHandler;

    @Resource
    PublishHandler publishHandler;

    @Resource
    PubRecHandler pubRecHandler;

    @Resource
    PubRelHandler pubRelHandler;

    @Resource
    SubscribeHandler subscribeHandler;

    @Resource
    UnSubscribeHandler unSubscribeHandler;





}

package com.wm.servicemqtt.mqtt.conf;


import com.wm.servicemqtt.mqtt.receiveHandler.MqttCallbackHandle;
import com.wm.servicemqtt.mqtt.topic.TopicName;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * 描述:
 * mqtt生成者配置
 *
 * @auther WangMin
 * @create 2020-07-30 9:02
 */
@Configuration
@IntegrationComponentScan
@Slf4j
public class MqttConfig {

    @Value("${mqtt.username}")
    private String username;

    @Value("${mqtt.password}")
    private String password;

    @Value("${mqtt.url}")
    private String hostUrl;


/************************************** connect *****************************************************/

    /**
     * 连接配置
     * @return
     */
    @Bean
    public MqttConnectOptions getMqttConnectOptions(){
        MqttConnectOptions mqttConnectOptions=new MqttConnectOptions();
        // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，
        // 这里设置为true表示每次连接到服务器都以新的身份连接
        mqttConnectOptions.setCleanSession(true);
        mqttConnectOptions.setUserName(username);
        mqttConnectOptions.setPassword(password.toCharArray());
        mqttConnectOptions.setServerURIs(StringUtils.split(hostUrl,","));
        // 设置超时时间 单位为秒
        mqttConnectOptions.setConnectionTimeout(10);
        // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送心跳判断客户端是否在线，但这个方法并没有重连的机制
        mqttConnectOptions.setKeepAliveInterval(20);
        // 设置“遗嘱”消息的话题，若客户端与服务器之间的连接意外中断，服务器将发布客户端的“遗嘱”消息。
        mqttConnectOptions.setWill(TopicName.DEFAULT_WILL_TOPIC.getValue(), "i.lost.connect".getBytes(), 2, false);
        return mqttConnectOptions;
    }

/************************************** producer *****************************************************/

    @Value("${mqtt.producer.clientId}")
    private String clientId;

    /**
     * 生产者通道生产工厂
     * @return
     */
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(getMqttConnectOptions());
        return factory;
    }

    /**
     * 生产者信息通道bean，绑定到MsgWriter
     * @return
     */
    @Bean(name = "mqttOutboundChannel")
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    /**
     * 处理bean名称为mqttOutboundChannel的通道的处理类
     * @return
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler messageHandler =  new MqttPahoMessageHandler(clientId, mqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic(TopicName.DEFAULT_PRODUCER_TOPIC.getValue());
        //解决当客户端订阅某一个主题时，会收到之前推送客户端发送的消息retained=false
        messageHandler.setDefaultRetained(false);
        return messageHandler;
    }

/************************************** consumer *****************************************************/

    @Value("${mqtt.consumer.clientId}")
    private String consumerId;

    @Resource
    private MqttCallbackHandle mqttCallbackHandle;

    @Bean
    public MessageProducerSupport mqttInbound() {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(consumerId,
                mqttClientFactory());
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());

        //默认添加TopicName中所有topic
        Arrays.stream(TopicName.values()).forEach(topicName -> adapter.addTopic(topicName.getValue(),2));

        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    //绑定到消费者
    @Bean(name="mqttInputChannel")
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }


    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return message -> {
            String topic = message.getHeaders().get("mqtt_receivedTopic").toString();
            String rec = message.getPayload().toString();
            log.info("rec:"+rec);
            mqttCallbackHandle.handle(topic,rec);
        };
    }


}

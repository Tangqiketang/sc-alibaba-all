package com.cetiti.iot.mqtt.config;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-04-01 14:15
 */
@Component
@ConfigurationProperties(prefix = "mqtt.broker")
@Data
@Accessors(chain = true)
public class BrokerProperties {

    /**
     * Broker唯一标识
     */
    private String id;
    private Boolean sslEnable = true;
    private Boolean normalEnable = true;
    /**
     * SSL端口号, 默认8883端口
     */
    private int sslPort = 8885;

    private int port = 1883;

    /**
     * WebSocket SSL端口号, 默认9993端口
     */
    private int websocketSslPort = 9995;

    /**
     * WebSocket Path值, 默认值 /mqtt
     */
    //private String websocketPath = "/mqtt";

    /**
     * SSL密钥文件密码
     */
    private String sslPassword;

    /**
     * 心跳时间(秒), 默认60秒, 该值可被客户端连接时相应配置覆盖
     */
    private int keepAlive = 60;

    /**
     * 是否开启Epoll模式, 默认关闭
     */
    private boolean useEpoll = false;

    /**
     * Sokcet参数, 存放已完成三次握手请求的队列最大长度, 默认512长度
     */
    private int soBacklog = 512;

    /**
     * Socket参数, 是否开启心跳保活机制, 默认开启
     */
    private Boolean soKeepAlive = true;
    /**
     * MQTT密钥验签有效期，-1时标识永不过期
     * */
    //private long passwordExpired = 1000*60*10;


}

package com.cetiti.iot.mqtt.broker;

import com.cetiti.iot.mqtt.config.BrokerProperties;
import com.cetiti.iot.mqtt.handler.HandlerFactory;
import com.cetiti.iot.mqtt.handler.MqttReceiveHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLEngine;
import java.io.InputStream;
import java.security.KeyStore;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-04-01 10:19
 */
@Service
@Slf4j
public class MqttSSLBrokerServer implements CommandLineRunner,AbstarctBroker{

    @Autowired
    private BrokerProperties brokerProperties;
    private SslContext sslContext;
    private EventLoopGroup sslBossGroup = null;
    private EventLoopGroup sslWorkerGroup = null;
    private Channel sslChannel = null;
    @Resource
    private HandlerFactory handlerFactory;

    @Override
    public void run(String... args) throws Exception {
        this.start();
    }
    @PreDestroy
    public void destroy(){
        this.stop();
    }


    @Override
    public void start() throws Exception {
        if(!brokerProperties.getSslEnable()){
            log.error("==============MQTT 启动ssl模式不支持===========");
            return;
        }
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("keystore/server.pfx");
        //证书生成时候的输入的密码
        keyStore.load(inputStream, brokerProperties.getSslPassword().toCharArray());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(keyStore, brokerProperties.getSslPassword().toCharArray());
        sslContext = SslContextBuilder.forServer(kmf).build();
        ServerBootstrap bootstrap = new ServerBootstrap();
        sslBossGroup = brokerProperties.isUseEpoll() ? new EpollEventLoopGroup() : new NioEventLoopGroup();
        sslWorkerGroup = brokerProperties.isUseEpoll() ? new EpollEventLoopGroup() : new NioEventLoopGroup();
        bootstrap.group(sslBossGroup, sslWorkerGroup)
                .channel(brokerProperties.isUseEpoll() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        p.addFirst("idle", new IdleStateHandler(0, 0, brokerProperties.getKeepAlive()));

                        SSLEngine sslEngine = sslContext.newEngine(ch.alloc());
                        sslEngine.setUseClientMode(false); // 使用服务端模式
                        sslEngine.setNeedClientAuth(false); // 不需要验证客户端
                        p.addLast("ssl", new SslHandler(sslEngine));

                        p.addLast(new MqttDecoder());
                        p.addLast(MqttEncoder.INSTANCE);
                        //处理类中构造进具体的处理bean工厂
                        p.addLast(new MqttReceiveHandler(handlerFactory));
                    }
                })
                .option(ChannelOption.SO_BACKLOG, brokerProperties.getSoBacklog())
                .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE);

        sslChannel = bootstrap.bind(brokerProperties.getSslPort()).sync().channel();
        log.info("MQTT SSL 启动，监听端口:{}", brokerProperties.getSslPort());


    }

    @Override
    public void stop() {

    }



}

package com.wm.web.netty.tcp;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.wm.web.netty.tcp.handler.MessageDecoder;
import com.wm.web.netty.tcp.handler.MessageHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.InetAddress;

/**
 * @author wangmin
 * @create 2023-09-19 9:47
 */
@Slf4j
@Component
public class NettyTcpServer implements ApplicationListener<ApplicationStartedEvent> {

    /**
     *  两个独立的Reactor线程池。
     * 一个用于接收客户端的TCP连接，
     * 另一个用于处理I/O相关的读写操作，或者执行系统Task、定时任务Task等。
     */
    private final EventLoopGroup bossGroup = new NioEventLoopGroup();
    private final EventLoopGroup workGroup = new NioEventLoopGroup();

    @Value("${netty.port:48080}")
    private int serverPort;

    @Resource
    private MessageHandler messageHandler;

    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        try {
            start();
            //把netty服务注册到nacos;注意命名空间问题还未解决
            registerNamingService("wm-netty-server", serverPort);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void start() throws InterruptedException{
        //ServerBootstrap负责初始化netty服务器，并且开始监听端口的socket请求
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workGroup)
                //非阻塞
                .channel(NioServerSocketChannel.class)
                //连接缓冲池大小
                .option(ChannelOption.SO_BACKLOG, 1024)
                //设置通道Channel的分配器
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new MessageDecoder())
                                .addLast(messageHandler);

                    }
                });
        ChannelFuture channelFuture = serverBootstrap.bind(serverPort).sync();
        //服务端启动监听事件
        channelFuture.addListener(future -> {
            if (future.isSuccess()) {
                log.info("TCP服务端启动成功,端口为:{}",serverPort);
            } else {
                log.info("TCP设备服务端启动失败,端口为:{}", serverPort);
            }
        });
    }

    /**
     * 注册到 nacos 服务中
     * @param serviceName netty服务名称
     * @param nettyPort netty服务端口
     */
    private void registerNamingService(String serviceName, Integer nettyPort) {
        try {
            NamingService namingService = NamingFactory.createNamingService(nacosDiscoveryProperties.getServerAddr());
            InetAddress address = InetAddress.getLocalHost();
            namingService.registerInstance(serviceName, address.getHostAddress(), nettyPort,nacosDiscoveryProperties.getNamespace());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}

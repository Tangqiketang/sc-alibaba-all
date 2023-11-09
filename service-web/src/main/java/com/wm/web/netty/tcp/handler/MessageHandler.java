package com.wm.web.netty.tcp.handler;

import com.alibaba.fastjson.JSON;
import com.wm.web.netty.tcp.cache.NettyContextCache;
import com.wm.web.netty.tcp.msg.CustomMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 *  业务处理
 */
@Slf4j
@ChannelHandler.Sharable
@Component
public class MessageHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
    }

    /**
     * @param ctx
     * @DESCRIPTION: 有客户端连接服务器会触发此函数
     * @return: void
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIp = insocket.getAddress().getHostAddress();
        int clientPort = insocket.getPort();
        String addr = NettyContextCache.parseChannelRemoteAddr(ctx.channel());
        //获取连接通道唯一标识
        ChannelId channelId = ctx.channel().id();

        //如果map中已经包含此连接，
        if (NettyContextCache.containsContext(channelId)) {
            log.info("【客户端:{}】,【channelId:{}】,该连接已经存在",addr,channelId);
        } else {
            //保存连接
            NettyContextCache.putChannelIdAndContext(channelId, ctx);
            log.info("【客户端:{}】,【channelId:{}】连接成功",addr,channelId);
        }
    }
    /**
     * @param ctx
     * @DESCRIPTION: 有客户端终止连接服务器会触发此函数
     * @return: void
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIp = insocket.getAddress().getHostAddress();
        ChannelId channelId = ctx.channel().id();
        String addr = NettyContextCache.parseChannelRemoteAddr(ctx.channel());
        if (NettyContextCache.containsContext(channelId)) {
            NettyContextCache.removeChannelId(channelId);
            log.info("【客户端{}】【channelId:{}】退出netty服务器",addr,channelId);
        }
    }


    /**
     * 描述:初始化
     * @param ctx 上下文
     * @param msg 消息
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        CustomMessage message = (CustomMessage) msg;


        log.info("【客户端:{}】【channelId:{}】接收到数据:【{}】",NettyContextCache.parseChannelRemoteAddr(ctx.channel()),ctx.channel().id(), JSON.toJSONString(message));
        String result = "收到";
        this.channelWrite(ctx.channel().id(), result);
    }

    /**
     * @param msg  需要发送的消息内容,自己的方法
     * @param channelId 连接通道唯一id
     * @DESCRIPTION: 服务端给客户端发送消息
     * @return: void
     */
    public void channelWrite(ChannelId channelId, String msg) throws Exception {
        ChannelHandlerContext ctx = NettyContextCache.getChannelContextByChannelId(channelId);
        if (ctx == null) {
            log.info("通道【{}】不存在",channelId);
            return;
        }
        if (msg == null && msg == "") {
            log.info("服务端响应空的消息");
            return;
        }

        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeByte(0x02);
        byteBuf.writeBytes(msg.getBytes());
        byteBuf.writeByte(0x03);

        ctx.writeAndFlush(byteBuf);
        //ctx.writeAndFlush(Unpooled.wrappedBuffer(msg.getBytes()));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        String socketString = NettyContextCache.parseChannelRemoteAddr(ctx.channel());

        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                log.info("Client:{}READER_IDLE 读超时",socketString);
                ctx.disconnect();
            } else if (event.state() == IdleState.WRITER_IDLE) {
                log.info("Client:{}WRITER_IDLE 写超时",socketString);
                ctx.disconnect();
            } else if (event.state() == IdleState.ALL_IDLE) {
                log.info("Client:{}ALL_IDLE 总超时",socketString);
                ctx.disconnect();
            }
        }
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        super.channelWritabilityChanged(ctx);
    }

    /**
     * @param ctx
     * @DESCRIPTION: 发生异常会触发此函数
     * @return: void
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        log.info("{}发生了错误,此连接被关闭" + "此时连通数量: {}",ctx.channel().id(),NettyContextCache.getConnectScoketCount());
    }

}

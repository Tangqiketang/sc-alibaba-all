package com.wm.web.netty.tcp.handler;

import cn.hutool.core.util.HexUtil;
import com.wm.web.netty.tcp.cache.NettyContextCache;
import com.wm.web.netty.tcp.msg.CustomMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

/**
 * 自定义长度帧解码器,处理粘包、消息解码。
 * 将原始数据变成对象后，返回下一层Handler处理
 */
@Slf4j
public class MessageDecoder extends LengthFieldBasedFrameDecoder {

    /**
     * 截取
     * 01 03  || 1C ||  00 00 03 E8 00 01 43 4f 00 00 00 00 00 01 00 00 00 00 00 64 00 00 03 E8 AA BB CC DD ||CRC1 CRC2
     *变成
     *                  00 00 03 E8 00 01 43 4f 00 00 00 00 00 01 00 00 00 00 00 64 00 00 03 E8 AA BB CC DD ||AB BA
     *
     *  magic|length|data|crc8 crc8
     *  魔数|长度域|数据域|crc
     *  在这个例子中 长度域的值= data的长度，不包含crc8
     */
    public MessageDecoder() {
        // maxFrameLength :  9527   最大帧长,发送的数据包最大长度
        // lengthFieldOffset :2     长度域开始的角标
        // lengthFieldLength :1     1个字节用于表示长度域
        // lengthAdjustment  :2     2表示把CRC1CRC2也带上。根据长度域的值，截取后面多长的数据，调整长度。0表示就截取length的长度。如果包含长度域的值包含长度域自身的长度1个字节，则为-1;如果包含crc1 crc2两个字节，增加+2
        //initialBytesToStrip:3     3表示舍弃最前面的magic+length
        super(9527, CustomMessage.FRAME_HEAD.length()/2, CustomMessage.lengthByteCount, 2, 3);
    }

    /**
     * 解码
     * @param ctx 上下文
     * @param buffer 消息体
     * @return 消息
     * @throws Exception
     */
    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
        //在super之前拿到的是原始数据,super调用了LengthFieldBasedFrameDecoder后截取为有效数据
        ByteBuf msgByteBuf = (ByteBuf) super.decode(ctx, buffer);
        if (msgByteBuf == null) {
            return null;
        }
        byte[] messageBytes = ByteBufUtil.getBytes(msgByteBuf);
        try {
            String msg = HexUtil.encodeHexStr(messageBytes,false);
            log.debug("【客户端:{}】,decode层得到消息:{}", NettyContextCache.parseChannelRemoteAddr(ctx.channel()),msg);
            return CustomMessage.parse(messageBytes);
        } catch (Exception e) {
            log.error("【客户端:{}】，decode层解析异常:{} error:{}",NettyContextCache.parseChannelRemoteAddr(ctx.channel()) ,HexUtil.encodeHexStr(messageBytes), e.getMessage(), e);
            ctx.close();
            return null;
        } finally {
            msgByteBuf.release();
        }
    }
}

package com.wm.web.netty.tcp.msg;

import cn.hutool.core.util.HexUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;

/**
 *   magic+length(不包含crc长度)+data+ crc1+crc2
 *
 *   原始上报完整报文         01 03     || 1C       ||  00 00 03 E8 00 01 43 4f 00 00 00 00 00 01 00 00 00 00 00 64 00 00 03 E8 AA BB CC DD ||CRC1 CRC2
 *   原始报文换成2进制数据即   0000 0001 ||0000 0011 || 0001 1100    ||data   || crc1,crc2
 *   1C=28表示data长度为28.不包含crc1 crc2
 *
 */

@Getter
@Setter
@Slf4j
public class CustomMessage {
    //用2字节表示magic
    public static final String FRAME_HEAD = "0103";
    //1C 用一个字节来表示真实有用消息体data的长度
    public static final int lengthByteCount = 1;

    private final String data;

    public CustomMessage(String data) {
        this.data = data;
    }

    private static String crc8(String hexString) {
        BigInteger sum = BigInteger.ZERO;
        for (int i = 0; i < hexString.length(); i += 2) {
            String hex = hexString.substring(i, Math.min(i + 2, hexString.length()));
            sum = sum.add(new BigInteger(hex, 16));
        }
        if (sum.compareTo(BigInteger.valueOf(0xFF)) > 0) {
            sum = sum.and(BigInteger.valueOf(0xFF));
        }
        String result = sum.toString(16);
        return result.length() == 2 ? result : "0" + result;
    }

    /**
     * 把接收到的二进制数据,变成16进制字符串存并入对象。
     */
    public static CustomMessage parse(byte[] messageBytes) {
        String hexStr = HexUtil.encodeHexStr(messageBytes, false);
        return new CustomMessage(hexStr);
    }

    /**
     * 从data的某个位置开始读数据,得到十六进制字符串
     *   00 00 03 E8 00 01 43 4f 00 00 00 00 00 01 00 00 00 00 00 64 00 00 03 E8 xx xx xx xx CRC1 CRC2
     * @param startByteIndex 从data某个起始位置开始读 从0开始;比如 2
     * @param byteCount      读几个字节的数据              ;比如 3
     * @return 返回读取的16进制字符串                       返回  03 E8 00
     */
    public String getDataOffsetHex(int startByteIndex, int byteCount) {
        return data.substring(startByteIndex * 2, startByteIndex * 2 + byteCount * 2);
    }

    /**
     * 从data的某个位置开始读数据，得到二进制byte数组
     * @param startByteIndex
     * @param byteCount
     * @return
     */
    public byte[] getDataOffsetBytes(int startByteIndex, int byteCount) {
        return HexUtil.decodeHex(getDataOffsetHex(startByteIndex, byteCount));
    }

/*    public String getDataOffsetStr(int startByteIndex, int byteCount, String charsetName) {
        try {
            return new String(getDataOffsetBytes(startByteIndex, byteCount), charsetName);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }*/

/*    public int getDataOffsetInt(int startByteIndex, int byteCount) {
        return Integer.parseInt(getDataOffsetHex(startByteIndex, byteCount), 16);
    }*/


}

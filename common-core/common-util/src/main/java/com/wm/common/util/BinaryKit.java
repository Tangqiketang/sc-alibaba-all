package com.wm.common.util;

import java.math.BigInteger;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-06-07 14:39
 */
public class BinaryKit {

    /**
     * 把int类型某一位设置为1
     * @param source
     * @param bitIndex
     * @return
     */
    public static int setIntSpecifiedBitTo1(int source, int bitIndex) {
        return  source | (1<<bitIndex);
    }
    /**
     * 把int类型某一位设置为0
     * @param source
     * @param bitIndex
     * @return
     */
    public static int setIntSpecifiedBitTo0(int source, int bitIndex) {
        return  source &= ~(1<<bitIndex);
    }

    /**
     * 把long类型某一位设置为1
     * @param source
     * @param bitIndex
     * @return
     */
    public static Long setLongSpecifiedBitTo1(Long source, int bitIndex) {
        return  source | (1l<<bitIndex);
    }
    /**
     * 把log类型某一位设置为0
     * @param source
     * @param bitIndex
     * @return
     */
    public static Long setLongSpecifiedBitTo0(Long source, int bitIndex) {
        return  source &= ~(1l<<bitIndex);
    }


    /**
     * 取多少位1
     * @param nBits  5
     * @return 1111 1
     */
    public static long getnBits(long nBits){
        //-1L: 64个111111111
        //-1L<<nBits 11111+n个00
        return -1L ^ (-1L << nBits);
    }


    /**
     *截取byte[]数组返回新的数组
     * @param source [00000000, 00000001,00000010,00000011]
     * @param start  1
     * @param length  2
     * @return [00000001,00000010]
     */
    public static byte[] copyByteArr(byte[] source,int start,int length){
        byte[] result = new byte[length];
        System.arraycopy(source,start,result,0,length);
        return result;
    }

    /************************************进制转换*********************************************/
    /**
     * 2制度字符串转字节数组
     * @param bitStr 00000001 001
     * @return  [1,32]
     */
    public static byte[] bitStr2Bytes(String bitStr) {
        StringBuilder in = new StringBuilder(bitStr);
        int remainder = in.length() % 8;
        if (remainder > 0)
            for (int i = 0; i < 8 - remainder; i++)
                in.append("0");
        byte[] bts = new byte[in.length() / 8];
        for (int i = 0; i < bts.length; i++)
            bts[i] = (byte) Integer.parseInt(in.substring(i * 8, i * 8 + 8), 2);
        return bts;
    }
    /**
     * 2进制数组转2进制字符串(最前面会舍弃00)
     * @param bytes [1,11,17]
     * @return 1 00001011 00010001
     */
    public static String bytes2BitStr(byte[] bytes) {
        return byteToScaleBitStr(bytes,2);
    }
    /**
     * 2进制字符串转10进制数字
     * @param bitStr 011
     * @return 3
     */
    public static int bitStr2Decimal(String bitStr) {
        int result = Integer.parseInt(bitStr,2);
        return scale2Decimal(bitStr, 2);
    }
    /**
     * 将2进制数组转成16进制
     * @param buf [1,11,17]
     * @return  01 0B 11
     */
    public static String byte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase()+" ");
        }
        return sb.toString();
    }
    /**
     * 10进制数字转2进制字符串
     * @param number  10
     * @return 1010
     */
    public static String decimal2BitStr(int number) {
        return decimal2Scale(number, 2);
    }
    /**
     * 10进制数字转16进制字符串
     * @param number 10
     * @return a
     */
    public static String decimal2HexStr(int number){
        return Integer.toHexString(number);
    }
    /**
     * 16进制字符串转2进制数组
     * @param hexStr 0A0B
     * @return [10,11]
     */
    public static byte[] hexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
                    16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }
    /**
     * 16进制字符串转化2进制字符串
     * @param hexString 0A0B
     * @return 0000101000001011
     */
    public static String hexStr2BitStr(String hexString) {
        if (hexString == null || hexString.length() % 2 != 0) {
            return null;
        }
        String bString = "", tmp;
        for (int i = 0; i < hexString.length(); i++) {
            tmp = "0000" + Integer.toBinaryString(Integer.parseInt(hexString.substring(i, i + 1), 16));
            bString += tmp.substring(tmp.length() - 4);
        }
        return bString;
    }
    /**
     * 其他进制转十进制
     * @param number 字符串
     * @param scale  其他哪种进制
     * @return
     */
    public static int scale2Decimal(String number, int scale) {
        checkNumber(number);
        if (2 > scale || scale > 32) {
            throw new IllegalArgumentException("scale is not in range");
        }
        int total = 0;
        String[] ch = number.split("");
        int chLength = ch.length;
        for (int i = 0; i < chLength; i++) {
            total += Integer.valueOf(ch[i]) * Math.pow(scale, chLength - 1 - i);
        }
        return total;
    }
    /**
     * 十进制转其他进制
     *
     * @param number 十进制的数字
     * @param scale 要变成哪种进制
     * @return
     */
    public static String decimal2Scale(int number, int scale) {
        if (2 > scale || scale > 32) {
            throw new IllegalArgumentException("scale is not in range");
        }
        String result = "";
        while (0 != number) {
            result = number % scale + result;
            number = number / scale;
        }
        return result;
    }
    /**
     * byte组数转几进制字符串
     * @param bytes
     * @param scale
     * @return
     */
    public static String byteToScaleBitStr(byte[] bytes,int scale){
        return new BigInteger(1,bytes).toString(scale);
    }
    public static void checkNumber(String number) {
        String regexp = "^\\d+$";
        if (null == number || !number.matches(regexp)) {
            throw new IllegalArgumentException("input is not a number");
        }
    }
    /************************ ************************************************/
    // -1L 二进制 111111111111
    //
    public static void main(String[] args) {

    }

}

package com.wm.common.util;

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


    /************************ ************************************************/
    // -1L 二进制 111111111111
    //
    public static void main(String[] args) {
        System.out.println(getnBits(2));
    }

}

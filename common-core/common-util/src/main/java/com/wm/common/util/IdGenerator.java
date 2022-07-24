package com.wm.common.util;

import org.apache.commons.lang3.RandomStringUtils;

import java.lang.management.ManagementFactory;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 *
 * @auther WangMin
 * @create 2022-06-07 23:17
 */
public class IdGenerator {

    private static Sequence WORKER = new Sequence();
    /**
     * 高效分布式ID生成算法(sequence),基于Snowflake算法优化实现64位自增ID算法。
     * @return long类型64位
     */
    public static long getSequenceId() {
        return WORKER.nextId();
    }

    /**
     * 高效分布式ID生成算法(sequence),基于Snowflake算法优化实现64位自增ID算法。
     * @return long类型64位
     */
    public static String getSequenceIdStr() {
        return String.valueOf(WORKER.nextId());
    }



    /**
     * 生成n位字母数字随机数
     * @param bit
     * @return
     */
    public static String getRandomAlphanumeric(int bit){
        return RandomStringUtils.randomAlphanumeric(bit).toLowerCase(Locale.ROOT);
    }









    /********************************************测试多线程情况********************************************************/


    public static void main(String[] args) {

        System.out.println( ManagementFactory.getRuntimeMXBean().getName());


        Set<String> total = new HashSet<>();

        for(int i=0;i<100;i++){
            Thread t = new Thread(){
                @Override
                public void run() {
                    String id = getSequenceIdStr();
                    System.out.println(id);
                    total.add(id);
                }
            };
            t.start();
            try {
                //等所有子线程执行完毕再执行主线程
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("total:"+total.size());


    }
}
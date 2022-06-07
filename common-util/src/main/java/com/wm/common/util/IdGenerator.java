package com.wm.common.util;

import java.util.HashSet;
import java.util.Set;

/**
 * 高效分布式ID生成算法(sequence),基于Snowflake算法优化实现64位自增ID算法。
 * 其中解决时间回拨问题的优化方案如下：
 * 1. 如果发现当前时间少于上次生成id的时间(时间回拨)，着计算回拨的时间差
 * 2. 如果时间差(offset)小于等于5ms，着等待 offset * 2 的时间再生成
 * 3. 如果offset大于5，则直接抛出异常
 *
 * @auther WangMin
 * @create 2022-06-07 23:17
 */
public class IdGenerator {
    private static Sequence WORKER = new Sequence();

    public static long getId() {
        return WORKER.nextId();
    }

    public static String getIdStr() {
        return String.valueOf(WORKER.nextId());
    }


    public static void main(String[] args) {
        Set<String> total = new HashSet<>();

        for(int i=0;i<100;i++){
            Thread t = new Thread(){
                @Override
                public void run() {
                    String id = getIdStr();
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
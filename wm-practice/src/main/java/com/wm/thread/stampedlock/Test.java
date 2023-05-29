package com.wm.thread.stampedlock;

import java.util.concurrent.locks.StampedLock;

/**
 * 描述: stampedlock在提供类似读写锁的同时，还支持优化读模式.
 *      优化读基于假设，先试着修改，然后通过validate方法看是否进入写模式，如果没有进入则成功避免了开销;
 *
 * @auther WangMin
 * @create 2023-04-17 14:47
 */
public class Test {

    private final StampedLock stampedLock = new StampedLock();

    public static void main(String[] args) {

    }

    void write(){
        long stamp = stampedLock.writeLock();
        try{
            //dowrite
        }finally {
            stampedLock.unlockWrite(stamp);
        }
    }

    String read(){
        long stamp = stampedLock.tryOptimisticRead();
        String readResult = doRead();
        if(!stampedLock.validate(stamp)){
            stamp = stampedLock.readLock();
            try{
                readResult = doRead();
            }finally {
                stampedLock.unlockRead(stamp);
            }
        }
        return readResult;
    }

    private String doRead() {
        return null;
    }


}
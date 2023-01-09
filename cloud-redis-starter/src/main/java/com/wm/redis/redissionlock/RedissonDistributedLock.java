package com.wm.redis.redissionlock;

import com.wm.redis.constant.RedisKeyConstant;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.util.concurrent.TimeUnit;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-06-22 17:18
 */
@ConditionalOnClass(RedissonClient.class)
@ConditionalOnProperty(prefix = "wm.redission.lock", name="enable",havingValue = "true",matchIfMissing = false)
public class RedissonDistributedLock implements DistributedLock{
    @Autowired
    private RedissonClient redisson;


    private ZLock getLock(String key, boolean isFair) {
        RLock lock;
        if (isFair) {
            lock = redisson.getFairLock(RedisKeyConstant.LOCK_KEY_PREFIX + ":" + key);
        } else {
            lock =  redisson.getLock(RedisKeyConstant.LOCK_KEY_PREFIX + ":" + key);
        }
        return new ZLock(lock, this);
    }

    @Override
    public ZLock lock(String key, long leaseTime, TimeUnit unit, boolean isFair) {
        ZLock zLock = getLock(key, isFair);
        RLock lock = (RLock)zLock.getLock();
        lock.lock(leaseTime, unit);
        return zLock;
    }

    @Override
    public ZLock tryLock(String key, long waitTime, long leaseTime, TimeUnit unit, boolean isFair) throws InterruptedException {
        ZLock zLock = getLock(key, isFair);
        RLock lock = (RLock)zLock.getLock();
        if (lock.tryLock(waitTime, leaseTime, unit)) {
            return zLock;
        }
        return null;
    }

    @Override
    public void unlock(Object lock) {
        if (lock != null) {
            if (lock instanceof RLock) {
                RLock rLock = (RLock)lock;
                if (rLock.isLocked()) {
                    rLock.unlock();
                }
            } else {
                throw new RuntimeException("requires RLock type");
            }
        }
    }

    public RReadWriteLock getReadWriteLock(String key){
        return redisson.getReadWriteLock(key);
    }

}
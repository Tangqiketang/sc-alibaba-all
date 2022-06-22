package com.wm.redis.redissionlock;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-06-22 17:19
 */
@AllArgsConstructor
public class ZLock  implements AutoCloseable {

    @Getter
    private final Object lock;

    private final DistributedLock locker;

    @Override
    public void close() throws Exception {
        locker.unlock(lock);
    }
}
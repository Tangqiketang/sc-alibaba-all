package com.wm.redis.spi;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-08-09 10:38
 */

public class TestSpiServiceImpl1 implements TestSpiService {
    @Override
    public String getServiceImplName() {
        return "name1";
    }
}
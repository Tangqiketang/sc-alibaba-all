package com.wm.web.spi;

import com.wm.redis.spi.TestSpiService;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-08-09 10:38
 */

public class TestSpiServiceImpl2 implements TestSpiService {
    @Override
    public String getServiceImplName() {
        return "name2";
    }
}
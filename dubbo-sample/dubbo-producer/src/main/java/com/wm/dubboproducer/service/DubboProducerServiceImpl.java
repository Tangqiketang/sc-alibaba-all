package com.wm.dubboproducer.service;

import com.wm.dubbo.common.service.DubboProducerService;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * 描述:
 *
 *
 *
 * @auther WangMin
 * @create 2022-05-31 16:30
 */
@DubboService(version = "1.0.0",interfaceClass = DubboProducerService.class,cluster = "failfast")
public class DubboProducerServiceImpl implements DubboProducerService {

    @Override
    public String test(String name) {
        return name+"xxxxxxxxxxx";
    }
}
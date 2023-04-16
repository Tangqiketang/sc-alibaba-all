package com.wm.dubboproducer.service;

import com.wm.core.model.exception.meta.ServiceException;
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
    public String test(String name){
        if(true){
            throw new ServiceException("10010","dubbo wm. 异常失败");
        }
        return name+"xxxxxxxxxxx";
    }
}
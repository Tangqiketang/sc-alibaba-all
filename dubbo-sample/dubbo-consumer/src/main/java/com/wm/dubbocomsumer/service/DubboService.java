package com.wm.dubbocomsumer.service;

import com.wm.dubbo.common.service.DubboProducerService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-05-31 17:52
 */
@Service
public class DubboService {


    @DubboReference(version = "1.0.0",loadbalance = "roundrobin",check = false)
    private DubboProducerService dubboProducerService;

    public String test(){
        return dubboProducerService.test("wm");
    }



}
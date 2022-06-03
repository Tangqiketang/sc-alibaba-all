package com.wm.servicefeign.controller;

import com.wm.servicefeign.service.feign.ServiceHiByFeign;
import com.wm.servicefeign.service.loadbalancer.ServiceHiByLoadBalancer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-02-13 0:12
 */
@RestController
@RefreshScope
public class HiController {

    @Resource
    private ServiceHiByLoadBalancer serviceHiByLoadBalancer;
    @Resource
    private ServiceHiByFeign serviceHiByFeign;

    @Value("${wm.configtest}")
    private String configTest;

    //通过loadBalancer去调用其他服务
    @GetMapping(value = "/hi")
    public String hi(@RequestParam String name) {
        return serviceHiByLoadBalancer.hiserviceByLoadBalancer( name );
    }

    //通过feign去调用其他服务
    @GetMapping(value = "/hi2")
    public String hi2(@RequestParam String name) {
        return serviceHiByFeign.getHiFromServiceHi(name);
    }

    @GetMapping(value = "/config")
    public String config() {
        return configTest;
    }
}
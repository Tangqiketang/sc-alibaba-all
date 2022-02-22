package com.wm.servicefeign.service.loadbalancer;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-02-13 0:17
 */
@Service
public class ServiceHiByLoadBalancer {

    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Resource
    private RestTemplate restTemplate;

    // 以下注入负载均衡客户端LoadBalancerClient是一个接口,下面只有一个RibbonLoadBalancerClient实现类
    @Resource
    private LoadBalancerClient loadBalancerClient;


    public String hiserviceByLoadBalancer(String name){
        System.out.println("aaa:"+loadBalancerClient.choose("service-hi")+"+++++++++++++++");
        return restTemplate.getForObject("http://service-hi/hi?name="+name,String.class);
    }

    public String hiError(String name) {
        return "hi,"+name+",sorry,error!";
    }

}
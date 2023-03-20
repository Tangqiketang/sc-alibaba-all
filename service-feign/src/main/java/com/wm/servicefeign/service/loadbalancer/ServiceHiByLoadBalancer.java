package com.wm.servicefeign.service.loadbalancer;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
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

    @Bean(name = "bl")
    @LoadBalanced  //如果使用这个注解,则必须使用服务名称进行调用
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Resource(name = "bl")
    private RestTemplate restTemplate;

    // 以下注入负载均衡客户端LoadBalancerClient是一个接口,下面只有一个RibbonLoadBalancerClient实现类
    @Resource
    private LoadBalancerClient loadBalancerClient;


    //注意降级方法必须为static
    @SentinelResource(value="hiserviceByLoadBalancer",fallback = "hiError",fallbackClass = ServiceHiByLoadBalancer.class,
            blockHandler = "testBlockHandler",blockHandlerClass = ServiceHiByLoadBalancer.class)
    public String hiserviceByLoadBalancer(String name){
        System.out.println("aaa:"+loadBalancerClient.choose("service-hi")+"+++++++++++++++");
        return restTemplate.getForObject("http://service-hi/hi?name="+name,String.class);
    }

    public static String hiError(String name) {
        return "hi,"+name+",sorry,error!";
    }
    public static String testBlockHandler(String name, BlockException ex) {
        return "hi,"+name+",testBlockHandler!";
    }

}
package com.wm.servicemqtt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-02-22 16:28
 */
@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class ServiceMqttApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceMqttApplication.class,args);
    }

    @Value("${server.port}")
    String port;

    @RequestMapping("/hi")
    public String home(@RequestParam(value = "name", defaultValue = "wm") String name) {
        return "hi " + name + " ,i am from port:" + port;
    }

}

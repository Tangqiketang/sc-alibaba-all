package com.wm.servicehi;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RequestBody;
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
public class ServiceHiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceHiApplication.class,args);
    }

    @Value("${server.port}")
    String port;

    @RequestMapping("/hi")
    public String home(@RequestParam(value = "name", defaultValue = "wm") String name) {
        return "hi " + name + " ,i am from port:" + port;
    }

    @RequestMapping("/hibody")
    public String homebody(@RequestBody Student student) {
        return JSON.toJSONString(student);
    }


}

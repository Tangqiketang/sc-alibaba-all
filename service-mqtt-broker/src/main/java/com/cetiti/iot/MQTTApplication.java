package com.cetiti.iot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-04-01 10:23
 */
@SpringBootApplication
@EnableDiscoveryClient
public class MQTTApplication {

    public static void main(String[] args) {
        //elasticsearch自身的netty冲突
        System.setProperty("es.set.netty.runtime.available.processors", "false");


        SpringApplication.run(MQTTApplication.class, args);

    }

}

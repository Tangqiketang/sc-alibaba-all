package com.wm.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-06-04 15:45
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableAsync
public class ServiceWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceWebApplication.class,args);
    }
}
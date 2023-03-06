package com.wm.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-06-04 15:45
 */
@SpringBootApplication
@EnableDiscoveryClient
public class WebReadWriteApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebReadWriteApplication.class,args);
    }
}
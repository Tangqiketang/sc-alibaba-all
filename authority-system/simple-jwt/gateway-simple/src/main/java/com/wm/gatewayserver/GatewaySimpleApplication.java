package com.wm.gatewayserver;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 描述:
 * 网关服务
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GatewaySimpleApplication {

    public static void main(String[] args) {
        SpringApplication.run( GatewaySimpleApplication.class, args );
    }




}

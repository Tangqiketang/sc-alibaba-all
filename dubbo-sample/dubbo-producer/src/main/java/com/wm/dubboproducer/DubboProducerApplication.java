package com.wm.dubboproducer;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-05-31 14:46
 */
@SpringBootApplication
@EnableDubbo
public class DubboProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DubboProducerApplication.class,args);
    }
}
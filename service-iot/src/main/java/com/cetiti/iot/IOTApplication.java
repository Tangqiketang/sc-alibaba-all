package com.cetiti.iot;

import com.cetiti.iot.rocketmq.output.MQOutput;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.stream.annotation.EnableBinding;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-04-01 10:23
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableBinding(MQOutput.class)
public class IOTApplication {

    public static void main(String[] args) {
        //elasticsearch自身的netty冲突
        System.setProperty("es.set.netty.runtime.available.processors", "false");


        SpringApplication.run(IOTApplication.class, args);

    }

}

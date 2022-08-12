package com.wm.rocketmq.producer;

import com.wm.rocketmq.producer.sender.RocketSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-08-09 16:12
 */
@SpringBootApplication
@RestController
@Slf4j
public class RocketmqProduerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RocketmqProduerApplication.class, args);
    }

    @Resource
    RocketSender rocketSender;


    @GetMapping("/testTopic1")
    public String sendTopic1() {
        rocketSender.sendWM1();
        return "success";
    }
    //tag
    @GetMapping("/testTopic2")
    public String sendTopic2Tag() {
        rocketSender.sendTopic2Tag();
        return "success";
    }
    //批量
    @GetMapping("/testTopic3")
    public String sendTopic3() {
        rocketSender.sendTopic3Tag3ByOrder();
        return "success";
    }

}
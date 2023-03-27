package com.wm.rocketmq.producer;

import com.wm.rocketmq.producer.sender.RocketSender;
import com.wm.rocketmq.producer.transaction.OrderSender;
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
    @Resource
    OrderSender orderSender;

    @GetMapping("/testTopic1")
    public String sendTopic1() {
        rocketSender.sendWM1();
        return "success";
    }

    @GetMapping("/testTopic2")
    public String sendTopic2() {
        rocketSender.sendWM2();
        return "success";
    }

    @GetMapping("/testTopic3")
    public String sendTopic3() {
        rocketSender.sendWM3();
        return "success";
    }


    //tag
    @GetMapping("/testTopic2Tag")
    public String sendTopic2Tag() {
        rocketSender.sendTopic2Tag();
        return "success";
    }
    //批量
    @GetMapping("/sendTopic3Tag3ByOrder")
    public String sendTopic3Tag3ByOrder() {
        rocketSender.sendTopic3Tag3ByOrder();
        return "success";
    }

    @GetMapping("/reply")
    public String reply() {
        rocketSender.reply();
        return "success";
    }
    //事务
    @GetMapping("/tran")
    public String tran() {
        orderSender.asyncDecreaseStockTransaction(15158,"134652",111);
        return "success";
    }


}
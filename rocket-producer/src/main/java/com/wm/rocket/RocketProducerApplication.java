package com.wm.rocket;

import com.wm.rocket.output.MyOutput;
import com.wm.rocket.sender.RocketSendService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-03-26 21:41
 */
@SpringBootApplication
@EnableBinding(MyOutput.class)
@RestController
public class RocketProducerApplication{

    public static void main(String[] args) {
        SpringApplication.run(RocketProducerApplication.class, args);
    }

    @Resource
    private RocketSendService rocketSendService;
    @GetMapping("/testTopic4")
    public String sendTransaction() {
        rocketSendService.sendTransaction();
        return "success";
    }
}
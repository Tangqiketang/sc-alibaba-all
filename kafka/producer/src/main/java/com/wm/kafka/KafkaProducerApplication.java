package com.wm.kafka;

import com.wm.kafka.producer.ProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-08-09 16:12
 */
@SpringBootApplication
@RestController
@Slf4j
public class KafkaProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaProducerApplication.class, args);
    }


    @Resource
    private ProducerService producerService;
    //异步
    @GetMapping("/testTopic1")
    public String sendTopic1() {
        producerService.sendAsync();
        return "success";
    }
    //同步
    @GetMapping("/testTopic2")
    public String sendTopic2() throws ExecutionException, InterruptedException {
        producerService.sendSync();
        return "success";
    }

    //同步
    @GetMapping("/testTopic3")
    public String sendTopic3() {
        producerService.addMessage();
        return "success";
    }

    //事务
/*    @GetMapping("/testTopic4")
    public String sendTopic4() {
        producerService.sendTransaction();
        return "success";
    }*/


}
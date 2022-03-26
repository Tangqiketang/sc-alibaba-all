package com.wm.rocket;

import com.wm.rocket.output.MyOutput;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-03-26 21:41
 */
@SpringBootApplication
@EnableBinding(MyOutput.class)
public class RocketProducerApplication{

    public static void main(String[] args) {
        SpringApplication.run(RocketProducerApplication.class, args);
    }
}
package com.wm.rocket;

import com.wm.rocket.input.MyInput;
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
@EnableBinding(MyInput.class)
public class RocketConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RocketConsumerApplication.class, args);
    }
}
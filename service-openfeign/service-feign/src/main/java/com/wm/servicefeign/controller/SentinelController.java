package com.wm.servicefeign.controller;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-02-13 0:12
 */
@RestController
@RefreshScope
public class SentinelController {


    @GetMapping(value = "/order")
    public String hi() {
        return "下订单成功"+System.currentTimeMillis();
    }


    @GetMapping(value = "/pay")
    public String hi2() {
        try {
            Thread.sleep(500);
        }catch (Exception e){

        }
        return "支付成功"+System.currentTimeMillis();
    }





}
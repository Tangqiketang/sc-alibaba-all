package com.wm.rocket.listener;

import com.alibaba.fastjson.JSONObject;
import com.wm.rocket.input.MyInput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-03-26 23:53
 */
@Component
@Slf4j
public class RockerListener {

    @StreamListener(MyInput.WM1_INPUT)
    public void onMessage(@Payload JSONObject message) {
        log.info("wm1[线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
    }

    @StreamListener(MyInput.WM2_INPUT)
    public void onTrekMessage(String message) {
        log.info("wm2[线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
    }

}
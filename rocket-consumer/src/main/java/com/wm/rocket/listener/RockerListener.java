package com.wm.rocket.listener;

import com.alibaba.fastjson.JSONObject;
import com.wm.rocket.input.MyInput;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.context.IntegrationContextUtils;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.ErrorMessage;
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
    static int a = 1;

    @StreamListener(MyInput.WM1_INPUT)
    public void listenTopic1(@Payload JSONObject message) {
        log.info("wm1[线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
    }

    @StreamListener(MyInput.WM2_INPUT)
    public void listenTopic2(String message) {
        log.info("wm2[线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
        //System.out.println(a);
        //a++;
        //throw new RuntimeException("故意抛错");
    }

    @StreamListener(MyInput.WM3_INPUT)
    public void listenTopic3(String message) {
        log.info("wm3[线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
    }

    /**********************异常处理****************************************/
    @ServiceActivator(inputChannel = "WM2-TOPIC.wm2-consumer-group-topic-01.errors")
    public void handleError(ErrorMessage errorMessage) {
        log.error("wm[handleError][payload：{}]", ExceptionUtils.getRootCauseMessage(errorMessage.getPayload()));
        log.error("wm[handleError][originalMessage：{}]", errorMessage.getOriginalMessage());
        log.error("wm[handleError][headers：{}]", errorMessage.getHeaders());
    }


    @StreamListener(IntegrationContextUtils.ERROR_CHANNEL_BEAN_NAME) // errorChannel
    public void globalHandleError(ErrorMessage errorMessage) {
        log.error("wm[globalHandleError][payload：{}]", ExceptionUtils.getRootCauseMessage(errorMessage.getPayload()));
        log.error("wm[globalHandleError][originalMessage：{}]", errorMessage.getOriginalMessage());
        log.error("wm[globalHandleError][headers：{}]", errorMessage.getHeaders());
    }

}
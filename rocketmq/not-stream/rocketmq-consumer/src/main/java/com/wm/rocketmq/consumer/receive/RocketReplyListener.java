package com.wm.rocketmq.consumer.receive;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQReplyListener;
import org.springframework.stereotype.Component;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-08-22 15:55
 */
@Component
@RocketMQMessageListener(topic = "wm_reply_topic", consumerGroup = "wm_reply_group")
@Slf4j
public class RocketReplyListener implements RocketMQReplyListener<String, String> {


    @Override
    public String onMessage(String message) {
        log.info("接受到消息:" + message);
        String ss = "success";
        // 返回消息到生成者
        return ss;
    }
}
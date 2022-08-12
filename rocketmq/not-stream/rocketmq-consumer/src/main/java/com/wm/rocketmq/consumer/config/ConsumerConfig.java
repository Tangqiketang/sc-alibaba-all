package com.wm.rocketmq.consumer.config;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述: 批量操作
 *
 * @auther WangMin
 * @create 2022-08-12 10:37
 */
@Configuration
@Slf4j
public class ConsumerConfig {

    @Bean
    public DefaultMQPushConsumer wm3MQPushConsumer(){
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("rocketmq-wm3-consumer1");
        consumer.setNamesrvAddr("192.168.40.131:9876");
        try {
            consumer.subscribe("WM3-TOPIC", "*");
        } catch (MQClientException e) {
            e.printStackTrace();
        }

        // 设置每次消息拉取的时间间隔，单位毫秒
        consumer.setPullInterval(2000);
        // 设置每个队列每次拉取的最大消息数
        consumer.setPullBatchSize(10);
        // 设置消费者单次批量消费的消息数目上限
        consumer.setConsumeMessageBatchMaxSize(12);
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context)
                -> {
            List<JSONObject> userInfos = new ArrayList<>(msgs.size());
            Map<Integer, Integer> queueMsgMap = new HashMap<>(8);
            msgs.forEach(msg -> {
                userInfos.add((JSONObject) JSONObject.parse(msg.getBody()));
                queueMsgMap.compute(msg.getQueueId(), (key, val) -> val == null ? 1 : ++val);
            });
            //处理批量消息，如批量插入：userInfoMapper.insertBatch(userInfos);
            System.out.println("wm3:"+userInfos.size());

            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        try {
            consumer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
        return consumer;
    }


}
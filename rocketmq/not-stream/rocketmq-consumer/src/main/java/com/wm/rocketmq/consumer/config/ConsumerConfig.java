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
        // 设置从每个队列每次拉取的数量。实际最大消息数默认borker*4*250。 broker数量*队列数量*pullBatchSize
        consumer.setPullBatchSize(32);
        // 设置消费者单次批量消费的消息数目上限.默认1。
        consumer.setConsumeMessageBatchMaxSize(32);
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context)
                -> {

            try{
                List<JSONObject> userInfos = new ArrayList<>(msgs.size());
                Map<Integer, Integer> queueMsgMap = new HashMap<>(8);
                msgs.forEach(msg -> {
                    userInfos.add((JSONObject) JSONObject.parse(msg.getBody()));
                    queueMsgMap.compute(msg.getQueueId(), (key, val) -> val == null ? 1 : ++val);
                });
                //处理批量消息，如批量插入：userInfoMapper.insertBatch(userInfos);
                System.out.println(Thread.currentThread().getName()+"||"+System.currentTimeMillis()/1000+"||"+"wm3:"+userInfos.size());
            }catch (Exception e){
                //通知mq把消息放入重试队列中。推荐返回枚举。不过也支持返回null,或直接抛出异常。
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
            //成功
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
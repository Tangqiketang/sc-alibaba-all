package com.wm.rocketmq.consumer.receive;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.stereotype.Component;

/**
 * 描述: 不同的topic不能用同一个消费组！！
 * 需要获取多个tag时，使用||分隔："a||b||c"
 * @auther WangMin
 * @create 2022-08-09 16:41
 */
@Component
@Slf4j
@RocketMQMessageListener(topic = "WM1-TOPIC",selectorExpression = "*",consumerGroup = "rocketmq-wm1-consumer1")
public class RocketmqListenerWM1 implements RocketMQListener<JSONObject> , RocketMQPushConsumerLifecycleListener {


    @Override
    public void onMessage(JSONObject jsonObject) {
        //幂等性。将已经处理的消息存入redis库中，每次处理前先进行查询操作，判断当前消息是否成功处理。
        //每个队列轮询获取
        log.info("topic1"+Thread.currentThread().getName()+System.currentTimeMillis()/1000+"收到消息:"+jsonObject);
    }

    @Override
    public void prepareStart(DefaultMQPushConsumer consumer) {
        //consumer.setConsumeThreadMin(20);
        //consumer.setConsumeThreadMax(20);
        consumer.setConsumeThreadMin(4);
        consumer.setConsumeThreadMax(4);
        // 每次拉取的间隔，单位为毫秒
        consumer.setPullInterval(1000);
        // 每次拉取实际数量.默认32
        consumer.setPullBatchSize(32);
    }
}
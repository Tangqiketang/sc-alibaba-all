package com.wm.rocketmq.consumer.receive;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.stereotype.Component;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-08-09 16:41
 */
@Component
@Slf4j
@RocketMQMessageListener(topic = "WM2-TOPIC",selectorExpression = "*",consumerGroup = "rocketmq-wm2-consumer1")
public class RocketmqListenerWM2 implements RocketMQListener<JSONObject> , RocketMQPushConsumerLifecycleListener {


    @Override
    public void onMessage(JSONObject jsonObject) {
        //每个队列轮询获取
        log.info("topic2"+Thread.currentThread().getName()+"||"+System.currentTimeMillis()/1000+"收到消息:"+jsonObject);
    }

    @Override
    public void prepareStart(DefaultMQPushConsumer consumer) {
        //consumer.setConsumeThreadMin(20);
        //consumer.setConsumeThreadMax(20);
        consumer.setConsumeThreadMin(20);
        consumer.setConsumeThreadMax(20);
        // 每次拉取的间隔，单位为毫秒
        consumer.setPullInterval(2000);
        // 每次拉取实际数量.默认32*1*4。 实际拉取数量= pullBatchSize*broker数量*N个队列数量=2*1*4
        consumer.setPullBatchSize(2);
    }
}
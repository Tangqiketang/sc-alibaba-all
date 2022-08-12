package com.wm.rocketmq.consumer.receive;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;

/**
 * 描述:顺序消息。同一个队列中顺序
 *
 * topic下队列对应的是消费者个数。消费者必须小于等于队列数量
 *
 *
 * @auther WangMin
 * @create 2022-08-09 16:41
 */
//@Component
@Slf4j
//@RocketMQMessageListener(topic = "WM3-TOPIC",selectorExpression = "*",consumerGroup = "rocketmq-wm3-consumer1",consumeMode = ConsumeMode.ORDERLY)
public class RocketmqListenerWM3 implements RocketMQListener<JSONObject> , RocketMQPushConsumerLifecycleListener {


    @Override
    public void onMessage(JSONObject jsonObject) {
        //每个队列轮询获取
        log.info("topic3"+Thread.currentThread().getName()+System.currentTimeMillis()/1000+"收到消息:"+jsonObject);
    }

    @Override
    public void prepareStart(DefaultMQPushConsumer consumer) {
        //consumer.setConsumeThreadMin(20);
        //consumer.setConsumeThreadMax(20);
        consumer.setConsumeThreadMin(20);
        consumer.setConsumeThreadMax(20);

        // 每次拉取的间隔，单位为毫秒.默认0秒
        consumer.setPullInterval(1000);
        // 每次拉取实际数量.默认32
        consumer.setPullBatchSize(10);

    }
}
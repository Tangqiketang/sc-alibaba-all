package com.wm.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2023-04-09 0:07
 */
@Component
@Slf4j
@EnableScheduling
public class ConsumerService {

    @Resource
    private KafkaListenerEndpointRegistry registry;


    /**
     * 手动提交确认消息
     */
    @KafkaListener(topics = {"wm-topic-1"})   //监听同一个主题
    public void receive(ConsumerRecord<?, ?> consumer, Acknowledgment ack) {
        log.info("收到topic名称:" + consumer.topic() + ",key:" + consumer.key() + ",分区位置:" + consumer.partition()
                + ", 下标" + consumer.offset()+"msg:"+consumer.value());
        //手动提交签收数据
        ack.acknowledge();
    }


    @KafkaListener(topics = {"wm-topic-2"},id = "2",groupId = "test-consumer-group")   //监听同一个主题
    public void receive2(ConsumerRecord<?, ?> consumer, Acknowledgment ack) {
        log.info("收到topic名称:" + consumer.topic() + ",key:" + consumer.key() + ",分区位置:" + consumer.partition()
                + ", 下标" + consumer.offset()+"msg:"+consumer.value());
        //手动提交签收数据
        ack.acknowledge();
    }

    @KafkaListener(topics = {"wm-topic-tran1"},id="3",groupId = "test-consumer-group")   //监听同一个主题
    public void receiveTrans(ConsumerRecord<?, ?> consumer, Acknowledgment ack) {
        log.info("收到topic名称:" + consumer.topic() + ",key:" + consumer.key() + ",分区位置:" + consumer.partition()
                + ", 下标" + consumer.offset()+"msg:"+consumer.value());
        //手动提交签收数据
        ack.acknowledge();
    }

    /**************************************************************************************/

    @KafkaListener(id = "listener-wm-1",topics = {
            "wm-topic-batch" }, groupId = "test-consumer-group", containerFactory = "batchListenFactory")
    public void listenTaskStart(List<ConsumerRecord> recordList, Acknowledgment acknowledgment) {
        for (ConsumerRecord record : recordList) {
            String msg = record.value().toString();
            log.info("定时消费成功,{}",msg+System.currentTimeMillis()/1000);
        }
        //手动签收
        acknowledgment.acknowledge();
    }

    // 任务启动 每隔1分钟获取一次
    @Scheduled(cron = "0 0/1 * * * ?")
    public void taskStartListener() {
        log.info("开启<任务启动>监听");
        MessageListenerContainer containerStart = registry.getListenerContainer("listener-wm-1");
        if (!containerStart.isRunning()) {
            log.info("container没有启动，开始启动");
            containerStart.start();
        }
        // 恢复监听
        containerStart.resume();
        try {
            Thread.sleep(10 * 1000);// 暂停10秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("关闭<任务启动>监听");
        // 暂停监听
        MessageListenerContainer containerClose = registry.getListenerContainer("listener-wm-1");
        containerClose.pause();
    }

    /***********************************************************************************/

}
package com.wm.kafka.producer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2023-04-09 0:01
 */
@Component
@Slf4j
public class ProducerService {

    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;

    // 启动发送消息方法
    public void addMessage() {
        JSONObject msg = new JSONObject();
        msg.put("wmtopic-batch","msg");
        kafkaTemplate.send("wm-topic-batch",JSON.toJSONString(msg));
        //kafkaTemplate.send("topic",1,1000L,"key",msg);
    }
    //异步发送，使用回调
    public void sendAsync(){
        JSONObject msg = new JSONObject();
        msg.put("wmtopic1","msg");
        ProducerRecord msgstr = new ProducerRecord<>("wm-topic-1", JSON.toJSONString(msg));

        ListenableFuture<SendResult<String,Object>> future = kafkaTemplate.send(msgstr);
        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onFailure(Throwable throwable) {
                log.error("发送消息失败,本地存消息列表定时任务重发");
            }
            @Override
            public void onSuccess(SendResult<String, Object> stringObjectSendResult) {

            }
        });
    }
    //同步发送，get阻塞
    public void sendSync() throws ExecutionException, InterruptedException {
        JSONObject msg = new JSONObject();
        msg.put("wmtopic2","msg");

        ListenableFuture<SendResult<String,Object>> future = kafkaTemplate.send(
                new ProducerRecord<>("wm-topic-2", JSON.toJSONString(msg)));
        log.info("get阻塞同步发送的结果:{}",future.get().toString());
    }


    //事务发送
/*    @Transactional(rollbackFor = RuntimeException.class)
    public void sendTransaction(){
        long now = System.currentTimeMillis()/1000;
        kafkaTemplate.send("wm-topic-tran1","事务消息1"+now);
        if(true){
           throw new RuntimeException();
        }
        kafkaTemplate.send("wm-topic-tran1","事务消息2"+now);
    }*/

    //事务发送.配置得修改为all,加transxxxxx
/*    @Transactional
    public void sendTransaction(){
        long now = System.currentTimeMillis()/1000;
        kafkaTemplate.executeInTransaction(t->{
            t.sendDefault("wm-topic-tran1","事务消息1"+now);
            if(true){
               // throw new RuntimeException();
            }
            t.sendDefault("wm-topic-tran1","事务消息2"+now);
            return true;
        });
    }*/

}
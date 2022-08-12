package com.wm.rocketmq.producer.sender;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Random;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-08-09 16:17
 */
@Component
@Slf4j
public class RocketSender {

    @Resource
    RocketMQTemplate rocketMQTemplate;

    //同步消息
    public void sendWM1(){
        for(int i=0;i<50;i++){
            JSONObject msg = new JSONObject();
            msg.put("mycontent","wm01");
            Message<JSONObject> spingMsg = MessageBuilder.withPayload(msg).build();
            rocketMQTemplate.send("WM1-TOPIC",spingMsg);
        }
    }
    //异步
    public void sendWM2(){
        JSONObject msg = new JSONObject();
        msg.put("mycontent","wm02");
        Message<JSONObject> spingMsg = MessageBuilder.withPayload(msg).build();
        rocketMQTemplate.asyncSend("WM2-TOPIC", spingMsg, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) { }
            @Override
            public void onException(Throwable throwable) { log.error("xxxx"); }
        });
    }
    //不关心发送结果
    public void sendWM2OneWay(){
        JSONObject msg = new JSONObject();
        msg.put("mycontent","wm02");
        Message<JSONObject> spingMsg = MessageBuilder.withPayload(msg).build();
        rocketMQTemplate.sendOneWay("WM2-TOPIC", spingMsg);
    }

    //客户端实时发送，服务端会处理延迟。可用于订单延迟取消
    public void sendDelay() {
        JSONObject msg = new JSONObject();
        msg.put("mycontent", "wmDelay");
        //1s、 5s、 10s、 30s、 1m、2m、 3m、 4m、 5m、 6m、 7m、 8m、 9m、 10m、 20m、 30m、 1h、2h
        Message<JSONObject> springMsg = MessageBuilder.withPayload(msg).
                setHeader(MessageConst.PROPERTY_DELAY_TIME_LEVEL, "3").build(); //延迟级别3对应10秒
        rocketMQTemplate.send("WM2-TOPIC",springMsg);
    }

    /**
     * 非stream的方式，tag放在header中无法生效！！
     */
    public void sendTopic2Tag(){
        for (String tag : new String[]{"tag1", "tag2", "tag3"}) {
            JSONObject msg = new JSONObject();
            msg.put("mycontent","wmTagContent  "+tag);
            Message<JSONObject> springMsg = MessageBuilder.withPayload(msg)
                    .setHeader(MessageConst.PROPERTY_TAGS, tag) // 设置 Tag。
                    .build();
            rocketMQTemplate.send("WM2-TOPIC:"+tag,springMsg);
        }
    }

    /**顺序消费,按照id或则如orderId(不是消息id)hash之后进同一个队列 **/
    public void sendTopic3Tag3ByOrder(){
        Random random = new Random();
        int id = 3;
        for (int i=0;i<50;i++) {
            JSONObject msg = new JSONObject();
            msg.put("id",id);
            msg.put("i",i);
            msg.put("mycontent","wmTagContent  order test"+" tag3");
            Message<JSONObject> springMsg = MessageBuilder.withPayload(msg)
                    .setHeader(MessageConst.PROPERTY_TAGS, "tag3") // 设置 Tag
                    .build();

            //同一个id的会进入一个队列,分区有序。  但是全局有不同订单号,默认4个队列的情况下是不同订单并非有序。
            rocketMQTemplate.syncSendOrderly("WM3-TOPIC",springMsg,String.valueOf(id));

        }
    }

    /**
     * 事务消息
     * @return
     */
    public void sendTransaction() {
        JSONObject msg = new JSONObject();
        msg.put("id",new Random().nextInt());

        JSONObject args = new JSONObject();
        args.put("args1",1);
        args.put("args2","2");

        Message<JSONObject> springMsg = MessageBuilder.withPayload(msg)
                .setHeader("args", args.toJSONString()) // 设置 Tag
                .build();
        System.out.println("beforeSend:"+System.currentTimeMillis());
        rocketMQTemplate.send(springMsg);
        System.out.println("AfterSend:"+System.currentTimeMillis());

    }
}
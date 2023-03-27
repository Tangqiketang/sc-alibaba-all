package com.wm.rocket.sender;

import com.alibaba.fastjson.JSONObject;
import com.wm.rocket.output.MyOutput;
import org.apache.rocketmq.common.message.MessageConst;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Random;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-03-26 20:58
 */
@Component
public class RocketSendService {

    @Resource
    private MyOutput myOutput;


    public void sendWM1(){
        JSONObject msg = new JSONObject();
        msg.put("mycontent","wm01");
        Message<JSONObject> spingMsg = MessageBuilder.withPayload(msg).build();

        myOutput.wm1Output().send(spingMsg);
    }

    public void sendWM2(){
        JSONObject msg = new JSONObject();
        msg.put("mycontent","wm02");
        Message<JSONObject> spingMsg = MessageBuilder.withPayload(msg).build();

        myOutput.wm2Output().send(spingMsg);
    }

    //客户端实时发送，服务端会处理延迟。可用于订单延迟取消
    public void sendDelay(){
        JSONObject msg = new JSONObject();
        msg.put("mycontent","wmDelay");
        //1s、 5s、 10s、 30s、 1m、2m、 3m、 4m、 5m、 6m、 7m、 8m、 9m、 10m、 20m、 30m、 1h、2h
        Message<JSONObject> springMsg = MessageBuilder.withPayload(msg).
                setHeader(MessageConst.PROPERTY_DELAY_TIME_LEVEL,"3").build(); //延迟级别3对应10秒
        myOutput.wm2Output().send(springMsg);
    }


    public void sendTopic2Tag(){
        for (String tag : new String[]{"tag1", "tag2", "tag3"}) {
            JSONObject msg = new JSONObject();
            msg.put("mycontent","wmTagContent  "+tag);
            Message<JSONObject> springMsg = MessageBuilder.withPayload(msg)
                    .setHeader(MessageConst.PROPERTY_TAGS, tag) // 设置 Tag
                    .build();
            myOutput.wm2Output().send(springMsg);
        }
    }


    /**顺序消费,按照id或则如orderId(不是消息id)hash之后进同一个队列 **/
    public void sendTopic3Tag3ByOrder(){
        Random random = new Random();
        int id = random.nextInt(10000);
        for (int i=0;i<10;i++) {
            JSONObject msg = new JSONObject();
            msg.put("id",id);
            msg.put("i",i);
            msg.put("mycontent","wmTagContent  order test"+" tag3");
            Message<JSONObject> springMsg = MessageBuilder.withPayload(msg)
                    .setHeader(MessageConst.PROPERTY_TAGS, "tag3") // 设置 Tag
                    .build();
            myOutput.wm3Output().send(springMsg);
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
                .setHeader("args", args.toJSONString())
                .build();
        System.out.println("beforeSend:"+System.currentTimeMillis());
        myOutput.wm4Output().send(springMsg);
        System.out.println("AfterSend:"+System.currentTimeMillis());

    }






}
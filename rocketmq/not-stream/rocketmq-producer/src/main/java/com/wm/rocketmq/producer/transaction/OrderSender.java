package com.wm.rocketmq.producer.transaction;

import com.alibaba.fastjson.JSONObject;
import com.wm.redis.constant.BusinessTypeEnum;
import com.wm.redis.util.RedisKit;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2023-03-20 17:39
 */
@Component
public class OrderSender {

    @Resource
    private RocketMQTemplate rocketMQTemplate;
    @Resource
    private RedisKit redisKit;

    /**
     * 库存秒杀,预提交后会开始执行本地事务类
     * @param userId
     * @param productId
     * @param amount
     * @Describe 把要秒杀的库存放入redis,利用rocketmq事务机制
     * @return
     */
    public boolean asyncDecreaseStockTransaction(Integer userId,String productId,Integer amount){
        try{
            //args用于保存当前方法原始参数
            JSONObject args = new JSONObject();
            args.put("userId",userId);
            args.put("productId",productId);
            args.put("amount",amount);
            args.put("date",System.currentTimeMillis()/1000);
            //准备发送出去的消息体
            JSONObject payload = new JSONObject();
            payload.put("wmargs","wmargs");

            //注意添加到请求头里面的topic tag transactionid这些不是destination里面的，两者不冲突。
            //args有点threadlocal存变量的感觉，用于回调看需不需要
            rocketMQTemplate.sendMessageInTransaction(
                    "rocketmq-order:wmordertag",
                    MessageBuilder.withPayload(payload)
                                    .setHeader(RocketMQHeaders.TRANSACTION_ID,redisKit.generate(BusinessTypeEnum.ORDER,8))
                                    .setHeader(RocketMQHeaders.KEYS,"wmtransKey")
                                    .build(),
                    args
            );
        }catch (Exception e){
            return false;
        }
        return true;
    }


}
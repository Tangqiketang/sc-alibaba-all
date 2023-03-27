package com.wm.rocketmq.producer.transaction;

import com.alibaba.fastjson.JSONObject;
import com.wm.redis.util.RedisKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 描述:
 * OrderSender类发送成功后，会开始调用本类执行本地事务
 *
 * @auther WangMin
 * @create 2023-03-21 9:26
 */
@Slf4j
@Component
@RocketMQTransactionListener()
public class OrderLocalTransactionListener implements RocketMQLocalTransactionListener {

    @Resource
    private RedisKit redisKit;

    /**
     *
     * @param msg 发送出去的消息体
     * @param arg 发送出去
     * @return
     */
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        try{
            //payload是二进制需要转。 args可以直接转string
            String payload = new String((byte[]) msg.getPayload());
            //msg中获取topic、tag、txid
            String topic = (String)msg.getHeaders().get("rocketmq_"+RocketMQHeaders.TOPIC);
            String tag   = (String)msg.getHeaders().get("rocketmq_"+RocketMQHeaders.TAGS);
            String txid  = (String)msg.getHeaders().get(RocketMQHeaders.TRANSACTION_ID);

            this.createOrder(arg);
        }catch (Exception e){
            return RocketMQLocalTransactionState.ROLLBACK;
        }
        return RocketMQLocalTransactionState.COMMIT;
    }

    /**
     * executeLocalTransaction长时间不提交，broker回查方法
     * @param msg
     * @return
     */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        String txid  = (String)msg.getHeaders().get(RocketMQHeaders.TRANSACTION_ID);
        //从本地数据库表中查询事务状态,如果事务成功返回commit
        return RocketMQLocalTransactionState.COMMIT;
    }

    //方法在这个类里事务不会生效，仅仅demo
    @Transactional
    public void createOrder(Object arg) {
        JSONObject obj = JSONObject.parseObject((String) arg);
        Integer userId = obj.getInteger("userId");
        String  productId = obj.getString("productId");
        Integer amount = obj.getInteger("amount");

        //TODO 检验商品合法性、用户合法性
        if(false){
            throw new RuntimeException();
        }
        //从redis中减库存
        boolean result = this.cacheDecreaseStock(productId,amount);
        if(!result){
            throw new RuntimeException();
        }
        //TODO 订单入库、生成交易流水
    }

    private boolean cacheDecreaseStock(String productId, Integer amount) {
        long result = redisKit.decr("product_promo:"+productId,1);
        if(result>0){
            //扣库存成功，还剩下个数大于0
            //发送异步消息从数据库中扣减库存 TODO
            return true;
        }else if(result==0){
            //打上库存已售罄
            return true;
        }else{
            //失败
            return false;
        }

    }


}
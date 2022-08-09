package com.wm.rocket.transaction;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.messaging.Message;

/**
 * 描述:broker会通过这个类，回查生产者的事务状态。 只有这里返回提交或回滚，broker中消息才能被消费者消费，否则会暂时保存在broker中
 *
 * @auther WangMin
 * @create 2022-03-27 21:55
 */
@Slf4j
@RocketMQTransactionListener(txProducerGroup = "wmproducer-group4")
public class Topc4TransactionListener implements RocketMQLocalTransactionListener {


    /**执行此方法前，已经发送了一条事务消息到broker，但是消息对消费者还未可见 **/
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        String args = msg.getHeaders().get("args",String.class);
        try{
            Thread.sleep(5000);
        }catch (Exception e){

        }
        System.out.println("executeTime:"+System.currentTimeMillis());
        log.info("wm4[executeLocalTransaction][执行本地事务，消息：{} args：{}]", msg, args);

        //1存储一条id为msg的事务编号，状态为unknow到数据库
        //2调用具体的事务业务层，如果全部成功，更新msg状态；如果异常等失败则msg更新为回滚状态
        //3.查找数据库中事务状态并返回
        //return rollback, commit or unknown。 本来应该是commit这里故意设置为unknow
        return RocketMQLocalTransactionState.UNKNOWN;
    }

    /** 在事务消息长时间未提交或回滚时，broker会来查询**/
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        System.out.println("checkTime:"+System.currentTimeMillis());
        try{
            Thread.sleep(10000);
        }catch (Exception e){

        }
        //从数据库中查找msg这条事务的状态

        //通过 msg 消息，获得某个业务上的标识或者编号，然后去数据库中查询业务记录，从而判断该事务消息的状态是提交还是回滚。
        // return rollback, commit or unknown
        log.info("wm4[checkLocalTransaction][回查消息：{}]", msg);
        return RocketMQLocalTransactionState.COMMIT;
    }
}
package com.wm.web.event.listener;

import com.alibaba.fastjson.JSON;
import com.wm.web.event.uwb.UwbChangeEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author wangmin
 * @create 2023-11-12 23:02
 */
@Component
@Slf4j
public class AnotationTypeListener {

    //同步处理,报错之后，上层方法也失败
    @EventListener(UwbChangeEvent.class)
    @Order(1)
    public void secondReceive(UwbChangeEvent event){
        log.info("11111注解方式-同步接收:{}", JSON.toJSON(event));
        event.getSource().setEngineId("被同步111111111改变");
        //throw  new RuntimeException();
    }

    //异步处理。异步处理不会对其他异步或同步产生影响.入参是单独的对象。同步也不会对异步产生影响
    @Async
    @EventListener(UwbChangeEvent.class)
    @Order(2)
    public void secondReceive2(UwbChangeEvent event){
        log.info("2222注解方式-异步接收到:{}", JSON.toJSON(event));
        event.getSource().setEngineId("被异步22222222222改变");
        //throw  new RuntimeException();
    }


    @EventListener(UwbChangeEvent.class)
    @Order(3)
    @Async
    public void secondReceive3(UwbChangeEvent event){
        log.info("3333注解方式-异步接收到:{}", JSON.toJSON(event));
        event.getSource().setEngineId("被异步3333333333333改变");
        // throw  new RuntimeException();
    }

    @EventListener(UwbChangeEvent.class)
    @Order(4)
    public void secondReceive4(UwbChangeEvent event){
        log.info("444444444注解方式-同步接收到:{}", JSON.toJSON(event));
        event.getSource().setEngineId("被同步44444444改变");
        // throw  new RuntimeException();
    }


    //@Async
    //@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT,classes = UwbChangeEvent.class)
/*    public void secondReceive5(UwbChangeEvent event){
        log.info("注解方式接收到动火作业FireWorkListener:{}", JSON.toJSON(event));
        throw  new RuntimeException();
    }*/

}

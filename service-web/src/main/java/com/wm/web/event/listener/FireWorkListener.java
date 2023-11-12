package com.wm.web.event.listener;

import com.alibaba.fastjson.JSON;
import com.wm.web.event.uwb.UwbChangeEvent;
import com.wm.web.event.uwb.UwbSeatData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 同步方式，链式listener。
 * 如果异常
 * @author wangmin
 * @create 2023-11-10 13:39
 */

@Component
@Slf4j
@Order(1)
public class FireWorkListener implements ApplicationListener<UwbChangeEvent> {

    @Override
    public void onApplicationEvent(UwbChangeEvent uwbChangeEvent) {
        log.info("接收到动火作业FireWorkListener:{}", JSON.toJSON(uwbChangeEvent));
        UwbSeatData source = uwbChangeEvent.getSource();
        source.setZ("FireWorkerListener的改动");
    }



}


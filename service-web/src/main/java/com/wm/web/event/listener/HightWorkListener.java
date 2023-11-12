package com.wm.web.event.listener;

import com.alibaba.fastjson.JSON;
import com.wm.web.event.uwb.UwbChangeEvent;
import com.wm.web.event.uwb.UwbSeatData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * order 99最晚执行
 * @author wangmin
 * @create 2023-11-10 13:39
 */

@Component
@Slf4j
@Order(99)
public class HightWorkListener implements ApplicationListener<UwbChangeEvent> {


    @Override
    public void onApplicationEvent(UwbChangeEvent uwbChangeEvent) {
        log.info("接收到高处作业HightWorkListener:{}", JSON.toJSON(uwbChangeEvent));
        UwbSeatData source = uwbChangeEvent.getSource();
        source.setEngineId("被HightWorklistener改变");
    }
}


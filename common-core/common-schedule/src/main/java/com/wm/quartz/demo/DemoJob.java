package com.wm.quartz.demo;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.stereotype.Component;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-08-25 16:24
 */
@PersistJobDataAfterExecution
@Slf4j
@Component
public class DemoJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("DemoJob 开始执行:{}",System.currentTimeMillis()/1000);
    }

}
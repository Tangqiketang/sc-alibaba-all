package com.wm.thread.blockqueue;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-08-11 9:41
 */
@Slf4j
public class BlockQueueTest implements ApplicationRunner {


    static private LinkedBlockingDeque<List<JSONObject>> queue = new LinkedBlockingDeque<>(100000);

    static public void push(List<JSONObject> dto){
        queue.offer(dto);
    }

    static public void drainTo(List<List<JSONObject>> insertList,int maxSize){
        try{
            int queueSize = queue.size();
            if(queueSize>0){
                log.debug(Thread.currentThread().getName()+".before.drainTo.queue.size.prop:{}",queueSize);
            }
            queue.drainTo(insertList,maxSize);
            if(null!=insertList&&insertList.size()>0){
                log.debug(Thread.currentThread().getName()+"get.queue.size.prop:{}",insertList.size());
            }
        }catch (Exception e){
            log.error("iot.insertLog.from.queue.err.prop:{}",e.getMessage());
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        int threadNum= 3;
        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
        for (int i=0;i<threadNum;i++){
            executorService.submit(new SaveProp2Clickhouse());
        }
    }

    private class SaveProp2Clickhouse implements Runnable{

        @Override
        public void run() {
            while(true){
                try{
                    List<List<JSONObject>> insertList = new ArrayList<>();
                    drainTo(insertList,3000);
                    if(!CollectionUtils.isEmpty(insertList)){
                        //ILogService iLogService = SpringUtil.getBean(ILogService.class);
                        //iLogService.batchInsertProp(insertList);
                    }
                }catch (Exception e){
                    log.error("插入数据logerr:{}",e.getMessage());
                }
                try{
                    Thread.sleep(1000);
                }catch (Exception e){

                }
            }
        }
    }
}
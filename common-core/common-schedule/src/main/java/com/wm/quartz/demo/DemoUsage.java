package com.wm.quartz.demo;

import com.wm.quartz.core.CoreExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-08-25 16:38
 */
@Component
public class DemoUsage {

    private final static Logger log= LoggerFactory.getLogger(DemoUsage.class);

    @Resource
    CoreExecutor coreExecutor;

    /**
     * 添加任务
     */
    public void createJob(){
        coreExecutor.startJob("demoJob1", DemoJob.class,"*/10 * * * * ?");
    }

    /**
     * 取消任务
     */
    public void cancelJob(){
        coreExecutor.removeJob("demoJob1");
    }

    /**
     * 修改任务cron触发器
     */
    public void modifyJobTrigger(){
        coreExecutor.modifyJobTime("demoJob1","*/2 * * * * ?");
    }

    /**
     * 暂停任务
     */
    public void pause(){
        coreExecutor.pauseJob("demoJob1");
    }

    /**
     * 恢复任务
     */
    public void resume(){
        coreExecutor.resumeJob("demoJob1");
    }

    /**
     * 获取全部任务名列表
     */
    public List<String> list(){
        return coreExecutor.listAllJobName();
    }

}
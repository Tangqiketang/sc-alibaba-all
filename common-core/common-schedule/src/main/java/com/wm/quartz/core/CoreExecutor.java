package com.wm.quartz.core;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 描述: 用于页面修改展示、动态控制
 *
 * @auther WangMin
 * @create 2022-08-25 16:13
 */
@Component
@Slf4j
public class CoreExecutor {
    private final static String JOB_GROUP_NAME = "CORE_JOBGROUP";
    private final static String TRIGGER_GROUP_NAME = "CORE_TRIGGERGROUP";

    @Resource
    Scheduler scheduler;

    /**
     * 添加任务（可选是否覆盖已存在的任务）
     *
     * @param jobName
     * @param runClass
     * @param jobDataMap
     * @param cron
     * @param overwriteExistingJob
     * @return
     */
    public Date startJob(String jobName, Class runClass, Map<? extends String, ?> jobDataMap, String cron, boolean overwriteExistingJob) {
        JobDetail jobDetail = buildJob(jobName, JOB_GROUP_NAME, runClass, jobDataMap);

        Trigger trigger = buildCronTrigger(jobName, TRIGGER_GROUP_NAME, cron);
        if (overwriteExistingJob) {
            return startJob(jobDetail, trigger, true);
        } else {
            return startJob(jobDetail, trigger);
        }
    }

    public Date startJob(String jobName, Class runClass, String cron, boolean overwriteExistingJob) {
        return startJob(jobName, runClass, null, cron, overwriteExistingJob);
    }


    /**
     * 添加任务
     *
     * @param jobName
     * @param runClass
     * @param jobDataMap
     * @param cron
     * @return
     */
    public Date startJob(String jobName, Class runClass, Map<? extends String, ?> jobDataMap, String cron) {
        return startJob(jobName, runClass, jobDataMap, cron, false);
    }

    public Date startJob(String jobName, Class runClass, String cron) {
        return startJob(jobName, runClass, cron, false);
    }


    /**
     * 添加任务（可选是否覆盖已存在的任务）
     *
     * @param jobName
     * @param runClass
     * @param jobDataMap
     * @param simpleTrigger
     * @param overwriteExistingJob
     * @return
     */
    public Date startJob(String jobName, Class runClass, Map<? extends String, ?> jobDataMap, SimpleTrigger simpleTrigger, boolean overwriteExistingJob) {
        JobDetail jobDetail = buildJob(jobName, JOB_GROUP_NAME, runClass, jobDataMap);
        if (overwriteExistingJob) {
            return startJob(jobDetail, simpleTrigger, true);
        } else {
            return startJob(jobDetail, simpleTrigger);
        }
    }

    public Date startJob(String jobName, Class runClass, SimpleTrigger simpleTrigger, boolean overwriteExistingJob) {
        return startJob(jobName, runClass, null, simpleTrigger, overwriteExistingJob);
    }


    /**
     * 添加任务
     *
     * @param jobName
     * @param runClass
     * @param simpleTrigger
     * @return
     */
    public Date startJob(String jobName, Class runClass, Map<? extends String, ?> jobDataMap, SimpleTrigger simpleTrigger) {
        return startJob(jobName, runClass, jobDataMap, simpleTrigger, false);
    }


    public Date startJob(String jobName, Class runClass, SimpleTrigger simpleTrigger) {
        return startJob(jobName, runClass,null, simpleTrigger, false);
    }


    /**
     * 修改任务cron触发器
     *
     * @param jobName
     * @param newCron
     * @return
     */
    public Date modifyJobTime(String jobName, String newCron) {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, TRIGGER_GROUP_NAME);
        Date date = null;
        try {
            Trigger oldTrigger = scheduler.getTrigger(triggerKey);
            if (oldTrigger == null) {
                return null;
            }
            Trigger newTrigger = buildCronTrigger(oldTrigger.getKey().getName(), oldTrigger.getKey().getGroup(), newCron);
            // 按新的trigger重新设置job执行
            date = scheduler.rescheduleJob(triggerKey, newTrigger);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.info("任务触发器修改成功，任务下次运行时间：" + date);
        return date;
    }

    /**
     * 修改任务（删除旧任务，添加新任务）
     *
     * @param oldJobName
     * @param newJobName
     * @param jobDataMap
     * @param newRunClass
     * @param newCron
     * @return
     */
    public Date modifyJob(String oldJobName, String newJobName,  Class newRunClass,Map<? extends String, ?> jobDataMap, String newCron) {
        deleteTrigger(oldJobName, TRIGGER_GROUP_NAME);
        deleteJob(oldJobName, JOB_GROUP_NAME);
        return startJob(newJobName, newRunClass, jobDataMap,newCron);
    }

    public Date modifyJob(String oldJobName, String newJobName, Class newRunClass, String newCron) {
        return modifyJob(oldJobName, newJobName,  newRunClass,null, newCron);
    }


    /**
     * 删除任务
     *
     * @param jobName
     */
    public void removeJob(String jobName) {
        deleteTrigger(jobName, TRIGGER_GROUP_NAME);
        deleteJob(jobName, JOB_GROUP_NAME);
    }

    /**
     * 暂停任务
     *
     * @param jobName
     */
    public void pauseJob(String jobName) {
        JobKey jobKey = JobKey.jobKey(jobName, JOB_GROUP_NAME);
        try {
            scheduler.pauseJob(jobKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.info("任务暂停成功");
    }

    /**
     * 恢复任务
     *
     * @param jobName
     */
    public void resumeJob(String jobName) {
        JobKey jobKey = JobKey.jobKey(jobName, JOB_GROUP_NAME);
        try {
            scheduler.resumeJob(jobKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.info("任务恢复暂停成功");
    }


    /**
     * @param jobName
     * @return NONE: 不存在
     * NORMAL: 正常
     * PAUSED: 暂停
     * COMPLETE:完成
     * ERROR : 错误
     * BLOCKED : 阻塞
     */
    public String getJobStatus(String jobName) {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, TRIGGER_GROUP_NAME);
        String name = null;
        try {
            Trigger.TriggerState triggerState = scheduler.getTriggerState(triggerKey);
            name = triggerState.name();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return name;
    }


    /**
     * 获取全部任务名列表
     *
     * @return
     */
    public List<String> listAllJobName() {
        List<String> list = new ArrayList<>();
        try {
            for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(JOB_GROUP_NAME))) {
                String jobName = jobKey.getName();
//				List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
                list.add(jobName);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    private Date startJob(JobDetail jobDetail, Trigger trigger, boolean overwriteExistingJob) {
        if (overwriteExistingJob) {
            try {
                if (scheduler.checkExists(jobDetail.getKey())) {
                    deleteJob(jobDetail);
                }
                if (scheduler.checkExists(trigger.getKey())) {
                    deleteTrigger(trigger);
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return startJob(jobDetail, trigger);
    }

    private Date startJob(JobDetail jobDetail, Trigger trigger) {
        Date date = null;
        try {
            date = scheduler.scheduleJob(jobDetail, trigger);
        } catch (ObjectAlreadyExistsException e) {
            throw new RuntimeException("添加失败，已存在任务名-任务组名为[" + jobDetail.getKey().getName() + "-" + jobDetail.getKey().getGroup() + "]" + "任务，请先移除该任务后再添加");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.info("任务创建成功，新的任务name:" + jobDetail.getKey().getName() + "，" + "任务下一次执行时间" + date);
        return date;
    }


    private JobDetail buildJob(String jobName, String jobGroupName, Class jobClass, Map<? extends String, ?> jobDataMap) {
        //job定义： // 任务名，任务组，任务执行类
        JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(jobName, jobGroupName)
                .build();
        if (jobDataMap != null) {
            jobDetail.getJobDataMap().putAll(jobDataMap);
        }
        return jobDetail;
    }


    private Trigger buildCronTrigger(String triggerName, String triggerGroupName, String cron) {
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerName, triggerGroupName).withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
        return trigger;
    }

    private void deleteJob(JobDetail jobDetail) {
        deleteJob(jobDetail.getKey());
    }

    private void deleteJob(String jobName, String jobGroupName) {
        deleteJob(JobKey.jobKey(jobName, jobGroupName));
    }

    private void deleteJob(JobKey jobKey) {
        try {
            // 删除任务
            scheduler.deleteJob(jobKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.info("任务移除成功");
    }


    private void deleteTrigger(String triggerName, String triggerGroupName) {
        deleteTrigger(TriggerKey.triggerKey(triggerName, triggerGroupName));
    }

    private void deleteTrigger(Trigger trigger) {
        deleteTrigger(trigger.getKey());
    }

    private void deleteTrigger(TriggerKey triggerKey) {
        try {
            // 停止触发器
            scheduler.pauseTrigger(triggerKey);
            // 移除触发器
            scheduler.unscheduleJob(triggerKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.info("触发器移除成功");
    }

}
package com.wm.quartz.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-08-25 16:10
 */
@Configuration
public class QuartzConfig {

    /**
     * 现版本的quartz-springboot2存在bug (Springboot:2.0.4，Quartz:2.3.0)，
     * 无法读取yml配置文件中的"org.quartz.scheduler.instanceName"自定义设置的Scheduler实例名称，
     * 会在加载时使用默认的Bean name "quartzScheduler" 覆盖掉自定义配置。
     * 因此手动实现SchedulerFactoryBeanCustomizer接口，在Scheduler初始化之前，将instanceName设置先于其自动化配置
     * 执行
     * 实现方式参考QuartzAutoConfiguration，该Bean的加载会优先于QuartzAutoConfiguration内部定义的Bean
     * @param properties
     * @param dataSource
     * @param quartzDataSource
     * @param transactionManager
     * @return
     */
    @Bean(name = "schedulerFactoryBeanCustomizer")
    public SchedulerFactoryBeanCustomizer dataSourceCustomizer(QuartzProperties properties, DataSource dataSource, @QuartzDataSource ObjectProvider<DataSource> quartzDataSource, ObjectProvider<PlatformTransactionManager> transactionManager, @QuartzTransactionManager ObjectProvider<PlatformTransactionManager> quartzTransactionManager) {
        return (schedulerFactoryBean) -> {
            if (properties.getJobStoreType() == JobStoreType.JDBC) {
                DataSource dataSourceToUse = this.getDataSource(dataSource, quartzDataSource);
                schedulerFactoryBean.setDataSource(dataSourceToUse);
                schedulerFactoryBean.setSchedulerName(properties.getProperties().get("org.quartz.scheduler.instanceName"));
                schedulerFactoryBean.setOverwriteExistingJobs(true);
                PlatformTransactionManager txManager = this.getTransactionManager(transactionManager, quartzTransactionManager);
                if (txManager != null) {
                    schedulerFactoryBean.setTransactionManager(txManager);
                }
            }

        };
    }


    private static DataSource getDataSource(DataSource dataSource, ObjectProvider<DataSource> quartzDataSource) {
        DataSource dataSourceIfAvailable = quartzDataSource.getIfAvailable();
        return dataSourceIfAvailable != null ? dataSourceIfAvailable : dataSource;
    }

    private PlatformTransactionManager getTransactionManager(
            ObjectProvider<PlatformTransactionManager> transactionManager,
            ObjectProvider<PlatformTransactionManager> quartzTransactionManager) {
        PlatformTransactionManager transactionManagerIfAvailable = quartzTransactionManager.getIfAvailable();
        return (transactionManagerIfAvailable != null) ? transactionManagerIfAvailable
                : transactionManager.getIfUnique();
    }
}
package com.wm.common.config.threadpool;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author wangmin
 * @date 2018/12/13
 */
@Setter
@Getter
@EnableAsync(proxyTargetClass = true)
@Configuration
@ConditionalOnProperty(prefix = "wm.threadPool", name="enable",havingValue = "true",matchIfMissing = false)
public class AsycThreadPoolConfig {
    /**
     *  线程池维护线程的最小数量.
     */
    @Value("${threadPool.corePoolSize:10}")
    private int corePoolSize;
    /**
     *  线程池维护线程的最大数量
     */
    @Value("${threadPool.maxPoolSize:100}")
    private int maxPoolSize;
    /**
     *  队列最大长度
     */
    @Value("${threadPool.queueCapacity:10}")
    private int queueCapacity;
    /**
     *  线程池前缀
     */
    @Value("${threadPool.nameFix:wmAsycExecutor-}")
    private String threadNamePrefix;

    @Bean(name = "wmTaskExecutor")
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new CustomThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix(threadNamePrefix);
        // 设置线程生存时间（秒）,当超过了核心线程出之外的线程在生存时间到达之后会被销毁
        executor.setKeepAliveSeconds(60);
        /*如果添加到线程池失败，那么主线程会自己去执行该任务，不会等待线程池中的线程去执行*/
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();
        return executor;
    }
}

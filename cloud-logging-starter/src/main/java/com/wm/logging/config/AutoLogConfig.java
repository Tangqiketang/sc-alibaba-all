package com.wm.logging.config;

import com.wm.logging.aspect.LogAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 描述:
 * 日志组件配置
 *
 * @auther WangMin
 * @create 2022-03-21 15:19
 */
@Configuration
public class AutoLogConfig {

    @Bean
    public LogAspect getLogAspect(){
        return new LogAspect();
    }

}

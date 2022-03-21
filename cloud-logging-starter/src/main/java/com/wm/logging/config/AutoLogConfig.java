package com.wm.logging.config;

import com.wm.logging.aspect.LogAspect;
import com.wm.logging.enable.LogMarkerConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
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
@ConditionalOnBean(LogMarkerConfiguration.class) //当容器中有这个xxbean就会使得下面配置生效
public class AutoLogConfig {

    @Bean
    public LogAspect getLogAspect(){
        return new LogAspect();
    }

}

package com.wm.redis.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * 描述:自定义缓存key,用于spring cache
 *
 * @auther WangMin
 * @create 2022-06-22 11:03
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "wm.cache-manager")
public class CacheManagerProperties {

    private List<CacheConfig> configs;

    @Setter
    @Getter
    public static class CacheConfig {
        private String key;

        private long second = 60;
    }

}
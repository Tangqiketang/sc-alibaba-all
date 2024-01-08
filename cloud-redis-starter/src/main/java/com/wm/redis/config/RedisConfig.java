package com.wm.redis.config;

import com.wm.redis.properties.CacheManagerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 描述:redis配置及spring cachemanager
 *
 * @auther WangMin
 * @create 2022-06-22 10:59
 */
@Configuration
@EnableCaching
@EnableConfigurationProperties({RedisProperties.class, CacheManagerProperties.class})
public class RedisConfig extends CachingConfigurerSupport {

    //缓存增加前缀
    private static final String springCachePrex = "wm-springcache";

    //key value的序列化方式
    @Bean
    public RedisSerializer<String> redisKeySerializer() {
        return RedisSerializer.string();
    }
    @Bean
    public RedisSerializer<Object> redisValueSerializer() {
        return RedisSerializer.json();
    }
    /************************************redis config ***********************************************************/
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory
            , RedisSerializer<String> redisKeySerializer, RedisSerializer<Object> redisValueSerializer) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        redisTemplate.setDefaultSerializer(redisValueSerializer);
        redisTemplate.setKeySerializer(redisKeySerializer);
        redisTemplate.setHashKeySerializer(redisKeySerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }


    /***************************************** spring cacheManager **********************************************************/
    //@Cacheable(value = "user", key = "#username")  //表示取出username作为key  放入
    //@CachePut(value = "user", key = "#sysUser.username", unless="#result == null")
    //@CacheEvict(value = "user", key = "#sysUser.username")   删除
    //@Cacheable(value = {"menuById"}, key = "#id", condition = "#conditionValue > 1")  condition
    //@Cacheable(cacheNames="缓存组件的名称",keyGenerator="myKeyGenerator")
    //@Cacheable(cacheNames = "auth", key = "'oauth-client:'+#clientId")

    //自定义key
    @Autowired
    private CacheManagerProperties cacheManagerProperties;


    @Bean(name = "cacheManager")
    @Primary
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory
            , RedisSerializer<String> redisKeySerializer, RedisSerializer<Object> redisValueSerializer) {
        RedisCacheConfiguration difConf = getDefConf(redisKeySerializer, redisValueSerializer).entryTtl(Duration.ofHours(1));

        //自定义的缓存过期时间配置.默认一小时。或者配置文件中配置
        int configSize = cacheManagerProperties.getConfigs() == null ? 0 : cacheManagerProperties.getConfigs().size();
        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>(configSize);
        if (configSize > 0) {
            cacheManagerProperties.getConfigs().forEach(e -> {
                RedisCacheConfiguration conf = getDefConf(redisKeySerializer, redisValueSerializer).entryTtl(Duration.ofSeconds(e.getSecond()));
                redisCacheConfigurationMap.put(e.getKey(), conf);
            });
        }

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(difConf)
                .withInitialCacheConfigurations(redisCacheConfigurationMap)
                .build();
    }


    @Bean("myKeyGenerator")
    public KeyGenerator keyGenerator() {
        return (target, method, objects) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName());
            sb.append(":" + method.getName() + ":");
            if(!Objects.isNull(objects)){
                for (Object obj : objects) {
                    if(null!=obj){
                        sb.append(obj.toString());
                    }
                }
            }
            return sb.toString();
        };
    }



    private RedisCacheConfiguration getDefConf(RedisSerializer<String> redisKeySerializer, RedisSerializer<Object> redisValueSerializer) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                //.computePrefixWith(cacheName -> springCachePrex.concat(":").concat(cacheName).concat(":"))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisKeySerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisValueSerializer));
    }

}
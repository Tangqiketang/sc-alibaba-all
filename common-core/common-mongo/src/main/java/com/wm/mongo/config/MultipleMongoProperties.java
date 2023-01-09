package com.wm.mongo.config;

import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2023-01-09 12:48
 */
@Configuration
public class MultipleMongoProperties {

    public static final String PRIMARY_MONGOP_ROPERTIES = "primaryMongoProperties";

    @Bean(name=PRIMARY_MONGOP_ROPERTIES)
    @Primary
    @ConfigurationProperties(prefix = "spring.data.mongodb.primary")
    public MongoProperties primaryMongoProperties() {
        return new MongoProperties();
    }

}
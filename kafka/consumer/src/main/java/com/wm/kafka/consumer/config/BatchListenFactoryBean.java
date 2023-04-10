package com.wm.kafka.consumer.config;

import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2023-04-09 0:05
 */
@Configuration
public class BatchListenFactoryBean {

    /**
     * kafka监听工厂配置
     * @param configurer
     * @return
     */
    @Bean("batchListenFactory")
    public ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerContainerFactory(
            ConcurrentKafkaListenerContainerFactoryConfigurer configurer, ConsumerFactory consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        // 开启批量消费功能
        factory.setBatchListener(true);
        // 不自动启动,默认不开启？
        factory.setAutoStartup(false);
        configurer.configure(factory, consumerFactory);
        return factory;
    }

}
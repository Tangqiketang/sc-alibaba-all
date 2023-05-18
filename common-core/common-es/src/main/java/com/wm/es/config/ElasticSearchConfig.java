package com.wm.es.config;

import lombok.Setter;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2023-05-16 22:21
 */
@Configuration
@ConfigurationProperties(prefix = "es")
@Setter
public class ElasticSearchConfig {

    private String hostname;
    private int port;
    private String protocol;

    //请求的一些选项
    public static final RequestOptions COMMON_OPTIONS;

    static{
        RequestOptions.Builder builder=RequestOptions.DEFAULT.toBuilder();
        COMMON_OPTIONS=builder.build();
    }

    @Bean
    public RestHighLevelClient restHighLevelClient(){
        RestClientBuilder restClientBuilder=
                RestClient.builder(new HttpHost(hostname,port,protocol));

        RestHighLevelClient restHighLevelClient=
                new RestHighLevelClient(restClientBuilder);

        return restHighLevelClient;
    }


}
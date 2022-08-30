package com.wm.gatewayserver.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 描述:自定义缓存key,用于spring cache
 *
 * @auther WangMin
 * @create 2022-06-22 11:03
 */
@Component
@ConfigurationProperties(prefix = "security")
@Data
public class IgnoreUrls {

    private List<String> ignoreUrls;

}
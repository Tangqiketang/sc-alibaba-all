package com.wm.jwtauth.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-08-30 11:05
 */
@ConfigurationProperties(prefix = "token")
@Component
@Data
public class TokenProperty {

    private String secret;
    private long expire;
}
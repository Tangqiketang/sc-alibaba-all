package com.wm.jwtauth.config;

import com.wm.jwtauth.filter.JwtFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-08-30 14:10
 */
@Configuration
public class JwtConfig {

    @Bean
    public FilterRegistrationBean jwtFilterBeanRegistration(){
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(getJwtFilter());
        bean.addUrlPatterns("/*");
        bean.setName("jwtFilter");
        return bean;
    }

    @Bean
    public JwtFilter getJwtFilter(){
        return new JwtFilter();
    }

}
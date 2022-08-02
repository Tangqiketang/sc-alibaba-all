package com.wm.auth.security.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;

/**
 * Manager provider service的配置
 */
@Configuration
@EnableWebSecurity
@Slf4j
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private UserDetailsService sysUserDetailServiceImpl;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests().antMatchers("/oauth/**").permitAll()
                //menu这里虽然不拦截,其实在网关层已经做了拦截
                .antMatchers("/menu/**").permitAll()
                .antMatchers("/webjars/**", "/doc.html", "/swagger-resources/**", "/v2/api-docs","/swagger-ui.html").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable();
    }

    //Manager认证管理器。入口。AuthenticationManager接口的常用实现类ProviderManager 内部会维护一个List列表，存放多种认证方式
    //Manager根据grant_type-->选择xxxProvider-->各种service如SysUserDetailsService
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    //Manager中添加各种provider
    @Override
    public void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    /**************************************************password*******************************************************/
    //Provider绑定service. grant_type=password的模式使用SysUserDetailsService进行校验
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(sysUserDetailServiceImpl);
        provider.setPasswordEncoder(passwordEncoder());
        provider.setHideUserNotFoundExceptions(false); // 是否隐藏用户不存在异常，默认:true-隐藏；false-抛出异常；
        return provider;
    }
    /**
     * 密码解码器
     * 真正解码的时候会根据密码的前缀选择对应的encoder，例如：$2a 标识BCYPT算法加密。
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
    /*************************************************wechat**********************************************************************/

}

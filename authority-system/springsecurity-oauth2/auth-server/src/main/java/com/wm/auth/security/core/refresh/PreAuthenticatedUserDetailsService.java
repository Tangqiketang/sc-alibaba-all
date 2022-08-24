package com.wm.auth.security.core.refresh;

import com.wm.auth.security.core.user.SysUserDetailServiceImpl;
import com.wm.common.util.oauth2.RequestUtils;
import com.wm.common.util.oauth2.enums.AuthenticationIdentityEnum;
import com.wm.common.util.oauth2.enums.IBaseEnum;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 描述:refresh
 *
 * @auther WangMin
 * @create 2022-07-16 0:41
 */
@NoArgsConstructor
@Slf4j
@Component
public class PreAuthenticatedUserDetailsService<T extends Authentication> implements AuthenticationUserDetailsService<T>, InitializingBean {
    @Resource
    private SysUserDetailServiceImpl sysUserDetailServiceImpl;

    private Map<String, UserDetailsService> userDetailsServiceMap=new ConcurrentHashMap<>();
    public void setUserDetailsServiceMap(String name,UserDetailsService userDetailsService){
        userDetailsServiceMap.put(name,userDetailsService);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
    }

    @Override
    public UserDetails loadUserDetails(T authentication) throws UsernameNotFoundException {
        //客户端id
        String clientId = RequestUtils.getOAuth2ClientId();
        //用户标识扩展信息。可以是username,openId等用于实现多客户端多体系登录
        AuthenticationIdentityEnum authenticationIdentityEnum = IBaseEnum.getEnumByValue(RequestUtils.getAuthenticationIdentity(), AuthenticationIdentityEnum.class);
        //从map中获取多种认证方式中匹配的那种
        UserDetailsService userDetailsService = userDetailsServiceMap.get(clientId);
        if(null==userDetailsService){
            userDetailsService = sysUserDetailServiceImpl;
        }
        //可以根据clientId实现多种认证方式。这里默认使用password
        if(clientId.equals("xxx")){

        }else{
            switch (authenticationIdentityEnum){
                default:
                    return userDetailsService.loadUserByUsername(authentication.getName());
            }
        }

        return null;
    }
}
package com.wm.auth.security.config;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.wm.auth.security.core.refresh.PreAuthenticatedUserDetailsService;
import com.wm.auth.security.core.user.SysUserDetail;
import com.wm.auth.security.core.user.SysUserDetailServiceImpl;
import com.wm.auth.security.core.user.WxUserDetails;
import com.wm.auth.security.extension.wechat.WechatTokenGranter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

import javax.annotation.Resource;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 描述: 认证服务器配置
 *
 * @auther WangMin
 * @create 2022-07-15 17:39
 */
@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Resource
    private ClientDetailsService clientDetailsServiceImpl;
    @Resource
    private SysUserDetailServiceImpl sysUserDetailsServiceImpl;
    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private PreAuthenticatedUserDetailsService preAuthenticatedUserDetailsService;


    /**
     * 设置我们自己的客户端认证
     * @param clients
     */
    @Override
    @SneakyThrows
    public void configure(ClientDetailsServiceConfigurer clients) {
        clients.withClientDetails(clientDetailsServiceImpl);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        //token增强链路添加内容增强和加解密增强
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> tokenEnhancers = new ArrayList<>();
        tokenEnhancers.add(tokenEnhancer()); //token内容增强
        tokenEnhancers.add(jwtAccessTokenConverter());//token加解密增强
        tokenEnhancerChain.setTokenEnhancers(tokenEnhancers);
        //token存储模式设定
        endpoints.tokenStore(jwtTokenStore());
        // 获取原有默认授权模式(默认包含了 授权码模式、密码模式、客户端模式、简化模式)的授权者
        List<TokenGranter> granterList = new ArrayList<>(Arrays.asList(endpoints.getTokenGranter()));
        //这里可以添加自定义授权模式
        granterList.add(new WechatTokenGranter(endpoints.getTokenServices(),clientDetailsServiceImpl,
                endpoints.getOAuth2RequestFactory(),WechatTokenGranter.GRANT_TYPE,authenticationManager));

        CompositeTokenGranter compositeTokenGranter = new CompositeTokenGranter(granterList);
        endpoints
                .authenticationManager(authenticationManager)
                .tokenEnhancer(tokenEnhancerChain)
                .tokenGranter(compositeTokenGranter)

                .userDetailsService(sysUserDetailsServiceImpl).reuseRefreshTokens(true)
                .tokenServices(tokenServices(endpoints));
    }



    /**********************************************jwt相关***************************************************/

    //token存储模式： inmemory/jdbc/redis/jwt
    @Bean
    public JwtTokenStore jwtTokenStore(){
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    /**
     * 密钥库中获取密钥对(公钥+私钥)
     */
    @Bean
    public KeyPair keyPair() {
        KeyStoreKeyFactory factory = new KeyStoreKeyFactory(new ClassPathResource("jwt.jks"), "123456".toCharArray());
        KeyPair keyPair = factory.getKeyPair("jwt", "123456".toCharArray());
        return keyPair;
    }


    /**
     * Token加解密增强。
     * @return TokenEnhancer的子类
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(keyPair());
        return converter;
    }

    /**
     * Token内容增强。 返回的是TokenEnhancer
     *
     */
    @Bean
    public TokenEnhancer tokenEnhancer() {
        return (accessToken, authentication) -> {
            Map<String, Object> additionalInfo = MapUtil.newHashMap();
            Object principal = authentication.getUserAuthentication().getPrincipal();
            if (principal instanceof SysUserDetail) {
                SysUserDetail sysUserDetails = (SysUserDetail) principal;
                additionalInfo.put("userId", sysUserDetails.getUserId());
                additionalInfo.put("username", sysUserDetails.getUsername());
                additionalInfo.put("deptId", sysUserDetails.getDeptId());
                // 认证身份标识(username:用户名;)
                if (StrUtil.isNotBlank(sysUserDetails.getAuthenticationIdentity())) {
                    additionalInfo.put("authenticationIdentity", sysUserDetails.getAuthenticationIdentity());
                }
            }else if(principal instanceof WxUserDetails){
                WxUserDetails wxUserDetails = (WxUserDetails) principal;
                additionalInfo.put("phone",wxUserDetails.getPhone());
                //其他额外信息 TODO
                additionalInfo.put("authenticationIdentity", wxUserDetails.getAuthenticationIdentity());
            }/*else if (principal instanceof MemberUserDetails) {
                MemberUserDetails memberUserDetails = (MemberUserDetails) principal;
                additionalInfo.put("memberId", memberUserDetails.getMemberId());
                additionalInfo.put("username", memberUserDetails.getUsername());
                // 认证身份标识(mobile:手机号；openId:开放式认证系统唯一身份标识)
                if (StrUtil.isNotBlank(memberUserDetails.getAuthenticationIdentity())) {
                    additionalInfo.put("authenticationIdentity", memberUserDetails.getAuthenticationIdentity());
                }
            }*/
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
            return accessToken;
        };
    }

    /***********************************token刷新***************************************************************/
    public DefaultTokenServices tokenServices(AuthorizationServerEndpointsConfigurer endpoints) {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(endpoints.getTokenStore());
        tokenServices.setClientDetailsService(clientDetailsServiceImpl);

        TokenEnhancerChain tokenEnhancerChain = (TokenEnhancerChain) endpoints.getTokenEnhancer();
        tokenServices.setTokenEnhancer(tokenEnhancerChain);

        // 支持使用refresh token刷新access token
        tokenServices.setSupportRefreshToken(true);
        /** refresh_token有两种使用方式：重复使用(true)、非重复使用(false)，默认为true
         *  1 重复使用：access_token过期刷新时， refresh_token过期时间未改变，仍以初次生成的时间为准
         *  2 非重复使用：access_token过期刷新时， refresh_token过期时间延续，在refresh_token有效期内刷新便永不失效达到无需再次登录的目的
         */
        tokenServices.setReuseRefreshToken(true);

        // 刷新token模式下，重写预认证提供者替换其AuthenticationManager，可自定义根据客户端ID和认证方式区分用户体系获取认证用户信息
        PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
        provider.setPreAuthenticatedUserDetailsService(preAuthenticatedUserDetailsService);
        tokenServices.setAuthenticationManager(new ProviderManager(Arrays.asList(provider)));

        return tokenServices;

    }


}
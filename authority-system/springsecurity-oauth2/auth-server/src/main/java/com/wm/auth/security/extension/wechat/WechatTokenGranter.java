package com.wm.auth.security.extension.wechat;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 描述: 微信授权入口。实现了AbstractTokenGranter,将grant_type传入父类构造用于识别
 *
 * 整体认证流程
 * 1.首先通过grant_type判断使用哪种TokenGranter
 * 2.TokenGranter中调用authenticationManager,
 * 3.authenticationManager通过参数是tokenGranter来选择使用哪种provider，因为provider中提供了support哪种token的方法
 *
 * @auther WangMin
 * @create 2022-08-31 14:57
 */
public class WechatTokenGranter extends AbstractTokenGranter {

    public static final String GRANT_TYPE="wechat";

    private final AuthenticationManager authenticationManager;

    /**
     * 必须重写抽象父类有参构造方法.
     * @param tokenServices
     * @param clientDetailsService 客户端认证
     * @param requestFactory
     * @param grantType 认证类型 GRANT_TYPE=wechat写死传入
     */
    public WechatTokenGranter(AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory,  String grantType,AuthenticationManager authenticationManager) {
        super(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
        //把配置中authenticationManager传入
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        //http请求参数
        Map<String, String> parameters = new LinkedHashMap(tokenRequest.getRequestParameters());
        String jsCode = parameters.get("jsCode");
        String code = parameters.get("code");
        String encryptedData = parameters.get("encryptedData");
        String iv = parameters.get("iv");
        //未认证前
        Authentication authParam = new WechatAuthenticationToken(jsCode, encryptedData,iv,code);
        ((AbstractAuthenticationToken) authParam).setDetails(parameters);
        //认证中，调用WechatAuthenticationProvider中authenticate(通过token类型选择正确的provider)
        authParam = this.authenticationManager.authenticate(authParam);

        if (authParam != null && authParam.isAuthenticated()) {
            //认证成功
            OAuth2Request storedOAuth2Request = this.getRequestFactory().createOAuth2Request(client, tokenRequest);
            return new OAuth2Authentication(storedOAuth2Request, authParam);
        } else {
            // 认证失败
            throw new InvalidGrantException("Could not authenticate jsCode: " + jsCode);
        }

    }
}
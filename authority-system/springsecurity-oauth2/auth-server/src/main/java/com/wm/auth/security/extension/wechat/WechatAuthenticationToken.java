package com.wm.auth.security.extension.wechat;

import com.wm.auth.security.core.user.WxUserDetails;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.util.Collection;

/**
 * 描述:
 * 用于传递的用户信息
 *
 * @auther WangMin
 * @create 2022-08-31 15:33
 */
@Getter
@Setter
public class WechatAuthenticationToken extends AbstractAuthenticationToken {
    //扩展信息
    private String jsCode;
    private String code;
    private Object wxUserDetails;
    private String encryptedData;
    private String iv;

    /**
     * token校验之前的构造
     * @param jsCode
     * @param encryptedData
     * @param iv
     */
    public WechatAuthenticationToken(String jsCode, String encryptedData,String iv,String code) {
        super(null);
        this.jsCode = jsCode;
        this.encryptedData = encryptedData;
        this.iv=iv;
        this.code=code;
        setAuthenticated(false);
    }

    /**
     * 账号校验成功之后的token构建
     * @param wxUserDetails
     * @param authorities
     */
    public WechatAuthenticationToken(WxUserDetails wxUserDetails, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.wxUserDetails = wxUserDetails;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.wxUserDetails;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        Assert.isTrue(!isAuthenticated, "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        super.setAuthenticated(false);
    }

    public void eraseCredentials() {
        super.eraseCredentials();
    }
}
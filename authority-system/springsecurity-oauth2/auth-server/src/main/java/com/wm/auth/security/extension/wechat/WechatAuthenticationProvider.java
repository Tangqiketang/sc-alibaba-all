package com.wm.auth.security.extension.wechat;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import com.wm.auth.model.entity.SysWxUser;
import com.wm.auth.security.core.user.SysUserDetailServiceImpl;
import com.wm.auth.security.core.user.WxUserDetails;
import com.wm.core.model.exception.meta.ServiceException;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashSet;

/**
 * 描述:微信认证提供.通过@Bean方式注入
 *
 * @auther WangMin
 * @create 2022-08-31 14:37
 */
@Component
public class WechatAuthenticationProvider implements AuthenticationProvider {

    @Resource
    private WxMaService wxMaService;
    @Resource
    private SysUserDetailServiceImpl sysUserDetailsServiceImpl;


    /**
     * 被wechatTokenGranter中authmanager调用
     * @param authentication  即WechatAuthenticationToken,包含了请求参数等扩展信息
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        WechatAuthenticationToken authenticationToken = (WechatAuthenticationToken) authentication;
        //小程序微信登录获得的jsCode。 获取用户信息电话code
        String jsCode = authenticationToken.getJsCode();
        String code = authenticationToken.getCode();
        String encryptedData = authenticationToken.getEncryptedData();
        String iv = authenticationToken.getIv();
        WxMaJscode2SessionResult sessionInfo = null;
        try {
            sessionInfo = wxMaService.getUserService().getSessionInfo(jsCode);
        } catch (WxErrorException e) {
            throw new ServiceException("10010","jsCode获取sessionInfo失败");
        }
        String openid = sessionInfo.getOpenid();
        WxUserDetails wxUserDetails = sysUserDetailsServiceImpl.loadUserByOpenId(openid);
        String phone = null;
        if(null==wxUserDetails){
            //创建微信表用户
            SysWxUser sysWxUser = new SysWxUser();
            sysWxUser.setOpenId(openid);
            sysWxUser.setStatus(1);
            sysWxUser.setCreateTime(LocalDateTime.now());
            if(StringUtils.isNoneBlank(code)){
                try{
                    WxMaPhoneNumberInfo phoneNoInfo = wxMaService.getUserService().getNewPhoneNoInfo(code);
                    phone = phoneNoInfo.getPhoneNumber();
                    sysWxUser.setPhone(phone);
                }catch (WxErrorException e){
                    throw new ServiceException("10010","code获取手机号失败");
                }
            }
            sysWxUser.insert();
        }
        wxUserDetails = sysUserDetailsServiceImpl.loadUserByOpenId(openid);
        WechatAuthenticationToken result = new WechatAuthenticationToken(wxUserDetails, new HashSet<>());
        result.setDetails(authentication.getDetails());
        return result;
    }


    /**
     * WechatTOkenGranter中this.authenticationManager.authenticate( new WechatAuthenticationToken()  )
     * 表明本类中authenticate(Authentication authentication)支持的参数得是WechatAuthenticationToken
     *
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return WechatAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
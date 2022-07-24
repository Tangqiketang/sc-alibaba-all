package com.wm.auth.security.core.client;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wm.auth.mapper.OauthClientMapper;
import com.wm.auth.model.entity.OauthClient;
import com.wm.core.model.exception.meta.ServiceException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 描述:客户端认证
 *
 * @auther WangMin
 * @create 2022-07-15 17:43
 */
@Service("clientDetailsServiceImpl")
public class ClientDetailsServiceImpl implements ClientDetailsService {
    private static final String NOOP = "{noop}";

    @Resource
    private OauthClientMapper oauthClientMapper;


    @Cacheable(cacheNames = "auth", key = "'oauth-client:'+#clientId")
    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        //从数据库中获取客户端。此时密码是明文。使用加密方式加密后返回，用于后续密码校验 TODO
        OauthClient oauthClient = oauthClientMapper.selectOne(new LambdaQueryWrapper<OauthClient>().eq(OauthClient::getClientId,clientId));
        if(null==oauthClient){
            throw new ServiceException("10010","客户端不存在");
        }
        //构造一个默认BaseClientDetails返回
        BaseClientDetails result = new BaseClientDetails(
                oauthClient.getClientId(),
                oauthClient.getResourceIds(),  //blank
                oauthClient.getScope(),        //all
                oauthClient.getAuthorizedGrantTypes(),  //wechat,refresh_token
                oauthClient.getAuthorities(),    //blank
                oauthClient.getWebServerRedirectUri()   //blank
        );
        result.setClientSecret(NOOP+oauthClient.getClientSecret()); //{noop}123456, return后会与请求中参数进行对比
        result.setAccessTokenValiditySeconds(oauthClient.getAccessTokenValidity());  //秒
        result.setRefreshTokenValiditySeconds(oauthClient.getRefreshTokenValidity());
        return result;
    }
}
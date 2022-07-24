package com.wm.auth.controller;


import com.alibaba.fastjson.JSON;
import com.wm.common.util.oauth2.RequestUtils;
import com.wm.core.model.vo.base.BaseResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.Map;

/**
 * 认证接口
 * @author Wang Min
 * @since 2022-06-01
 */
@Controller
@RequestMapping("/oauth")
@Api(value = "认证",tags={"认证相关"})
@Slf4j
public class AuthController {

    @Resource
    private TokenEndpoint tokenEndpoint;


    @ApiOperation(value = "OAuth2认证", notes = "登录入口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "grant_type", defaultValue = "password", value = "授权模式", required = true),
            @ApiImplicitParam(name = "client_id", defaultValue = "1-app", value = "Oauth2客户端ID 1-app", required = true),
            @ApiImplicitParam(name = "client_secret", defaultValue = "123456", value = "Oauth2客户端秘钥", required = true),
            @ApiImplicitParam(name = "refresh_token", value = "刷新token"),
            @ApiImplicitParam(name = "username", defaultValue = "wm", value = "用户名"),
            @ApiImplicitParam(name = "password", defaultValue = "123456", value = "用户密码")
    })
    @ResponseBody
    @PostMapping("/token")
    public BaseResp postAccessToken(
            @ApiIgnore Principal principal,  //principal就是userservicedetail
            @ApiIgnore @RequestParam Map<String, String> parameters
    ) throws HttpRequestMethodNotSupportedException {

        log.info("principal:{}", JSON.toJSONString(principal));
        String clientId = RequestUtils.getOAuth2ClientId();

        OAuth2AccessToken accessToken = tokenEndpoint.postAccessToken(principal, parameters).getBody();

        return BaseResp.ok(accessToken);

    }


}


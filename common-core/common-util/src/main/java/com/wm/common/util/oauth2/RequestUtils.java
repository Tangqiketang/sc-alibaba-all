package com.wm.common.util.oauth2;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.nimbusds.jose.JWSObject;
import com.wm.common.util.oauth2.enums.AuthenticationIdentityEnum;
import com.wm.core.model.constants.AuthConstant;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 请求工具类
 *
 */
@Slf4j
public class RequestUtils {

    @SneakyThrows
    public static String getGrantType() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String grantType = request.getParameter("grant_type");
        return grantType;
    }

    /**
     * 获取登录认证的客户端ID
     * 兼容两种方式获取OAuth2客户端信息（client_id、client_secret）
     * 方式一：client_id、client_secret放在请求路径中
     * 方式二：放在请求头（Request Headers）中的Authorization字段，且经过加密，例如 Basic Y2xpZW50OnNlY3JldA== 明文等于 client:secret
     *
     * @return
     */
    @SneakyThrows
    public static String getOAuth2ClientId() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        // 从请求路径中获取url?client_id=xx
        String clientId = request.getParameter(AuthConstant.CLIENT_ID_KEY);
        if (StrUtil.isNotBlank(clientId)) {
            return clientId;
        }

        //从请求头中获取base64加密过的wm-client:123456
        String basic = request.getHeader(AuthConstant.AUTHORIZATION_KEY);
        if (StrUtil.isNotBlank(basic) && basic.startsWith(AuthConstant.BASIC_PREFIX)) {
            basic = basic.replace(AuthConstant.BASIC_PREFIX, Strings.EMPTY);
            String basicPlainText = new String(Base64.getDecoder().decode(basic.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
            clientId = basicPlainText.split(":")[0]; //client:secret
        }
        return clientId;
    }

    /**
     * 解析refresh JWT获取获取认证身份标识
     *
     * @return
     */
    @SneakyThrows
    public static String getAuthenticationIdentity() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String refreshToken = request.getParameter(AuthConstant.REFRESH_TOKEN_KEY);

        String payload = StrUtil.toString(JWSObject.parse(refreshToken).getPayload());
        JSONObject jsonObject = JSONUtil.parseObj(payload);

        String authenticationIdentity = jsonObject.getStr(AuthConstant.AUTHENTICATION_IDENTITY_KEY);
        if (StrUtil.isBlank(authenticationIdentity)) {
            authenticationIdentity = AuthenticationIdentityEnum.USERNAME.getValue(); //username
        }
        return authenticationIdentity;
    }
}

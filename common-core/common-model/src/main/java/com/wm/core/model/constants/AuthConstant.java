package com.wm.core.model.constants;

public interface AuthConstant {

    /**
     * 认证请求头key
     */
    String AUTHORIZATION_KEY = "Authorization";

    /**
     * JWT令牌前缀
     */
    String JWT_PREFIX = "Bearer ";


    /**
     * Basic认证前缀
     */
    String BASIC_PREFIX = "Basic ";

    /**
     * JWT内容中自带的唯一标识
     */
    String JWT_JTI = "jti";

    /**
     * JWT载体key。经常网关解密，把解密信息放入这个header中传到到后面的微服务
     */
    String JWT_PAYLOAD_KEY = "payload";


    //如果客户端id放在url后,使用这个参数
    String CLIENT_ID_KEY = "client_id";

    /**
     * JWT存储权限前缀
     */
    String AUTHORITY_ROLE_PREFIX = "ROLE_";


    /**
     * JWT中存储的权限属性
     */
    String JWT_AUTHORITIES_KEY = "authorities";


    //请求参数中的授权类型字段
    String GRANT_TYPE_KEY = "grant_type";

    String REFRESH_TOKEN_KEY = "refresh_token";

    /**
     * jwt解析出来的内容中身份标识
     */
    String AUTHENTICATION_IDENTITY_KEY = "authenticationIdentity";


}

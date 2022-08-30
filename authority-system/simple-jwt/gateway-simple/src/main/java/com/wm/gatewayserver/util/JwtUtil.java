package com.wm.gatewayserver.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.wm.core.model.constants.SimpleJwtConst;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-08-24 17:37
 */
@Component
public class JwtUtil {

    @Value("${token.expire}")
    private long EXPIRE_TIME;

    @Value("${token.secret}")
    private String secret;

    public Map<String, Object> createToken(String userId,String userName,String roleIds){
        Map<String,Object> tokenInfoMap = new HashMap<>();
        Algorithm algorithm = Algorithm.HMAC256(secret);
        long tokenExpire =System.currentTimeMillis() + EXPIRE_TIME;
        Date date = new Date(tokenExpire);
        String token = JWT.create().withClaim(SimpleJwtConst.auth.USER_NAME, userName)
                .withClaim(SimpleJwtConst.auth.USER_ID, userId)
                .withClaim(SimpleJwtConst.auth.USER_ROLES,roleIds)
                .withExpiresAt(date).sign(algorithm);
        tokenInfoMap.put(SimpleJwtConst.auth.TOKEN_KEY,token);
        tokenInfoMap.put(SimpleJwtConst.auth.TOKEN_EXPIRE_TIME,tokenExpire);
        return tokenInfoMap;

    };

    public DecodedJWT verifyToken(String token){
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).build();
        DecodedJWT jwt =verifier.verify(token);
        return jwt;
    }

}
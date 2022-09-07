package com.wm.jwtauth.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.wm.jwtauth.constants.ConfigConst;
import com.wm.jwtauth.property.TokenProperty;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-08-30 10:53
 */
@Component
public class JwtUtil {

    @Resource
    private TokenProperty tokenProperty;

    @Resource
    Environment environment;

    public Map<String, Object> createToken(String userId,String userName,String roleIds){
        //expireTime = environment.getProperty("token.expire",long.class);
        //secret = environment.getProperty("token.secret");
        Map<String,Object> tokenInfoMap = new HashMap<>();
        Algorithm algorithm = Algorithm.HMAC256(tokenProperty.getSecret());
        long tokenExpire =System.currentTimeMillis() + tokenProperty.getExpire();
        Date date = new Date(tokenExpire);
        String token = JWT.create().withClaim(ConfigConst.auth.USER_NAME, userName)
                .withClaim(ConfigConst.auth.USER_ID, userId)
                .withClaim(ConfigConst.auth.USER_ROLES,roleIds)
                .withExpiresAt(date).sign(algorithm);
        tokenInfoMap.put(ConfigConst.auth.TOKEN_KEY,token);
        tokenInfoMap.put(ConfigConst.auth.TOKEN_EXPIRE_TIME,tokenExpire);
        return tokenInfoMap;
    };

    public DecodedJWT verifyToken(String token){
        //secret = environment.getProperty("token.secret");
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(tokenProperty.getSecret())).build();
        DecodedJWT jwt =verifier.verify(token);
        return jwt;
    }
}
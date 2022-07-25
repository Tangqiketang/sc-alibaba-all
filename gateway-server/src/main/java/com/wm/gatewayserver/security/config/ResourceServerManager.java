package com.wm.gatewayserver.security.config;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.wm.core.model.constants.AuthConstant;
import com.wm.core.model.constants.RedisConstant;
import com.wm.redis.util.RedisKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 描述: 用于校验请求是否有权限。
 * 鉴权的token.
 *
 * @auther WangMin
 * @create 2022-07-07 14:09
 */
@Component
@Slf4j
public class ResourceServerManager implements ReactiveAuthorizationManager<AuthorizationContext> {
    @Resource
    private RedisKit redisKit;

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {
        ServerHttpRequest request = authorizationContext.getExchange().getRequest();
        if(request.getMethod()== HttpMethod.OPTIONS){
            //所有http预检请求放行
            return Mono.just(new AuthorizationDecision(true));
        }

        String token = request.getHeaders().getFirst(AuthConstant.AUTHORIZATION_KEY);
        if(StringUtils.isBlank(token)){
            return Mono.just(new AuthorizationDecision(false));
        }else{
            if(StringUtils.startsWithIgnoreCase(token,AuthConstant.JWT_PREFIX)){
                //如果请求头是Authorization。security会在在进这里之前就校验jwt的格式内容。
                if(false){
                    //有些项目可以免除鉴权,只需要认证就行
                    return Mono.just(new AuthorizationDecision(true));
                }
            }
        }
        /***********************开始鉴权******************************/
        //["PUT:/service-auth/user/*",   ["ADMIN"]]
        Map<String, Object> urlPermRolesRules =redisKit.opsForHash().entries(RedisConstant.GlobalAuth.URL_PERM_ROLES_KEY);
        //这个路径需要哪些角色可以访问
        List<String> authorizedRolesCan = new ArrayList<>();
        boolean requireCheck = false;

        PathMatcher pathMatcher = new AntPathMatcher();
        String method = request.getMethodValue();
        String path =request.getURI().getPath();
        String restfulPath =method+":"+path; //权限支持restful格式的url

        for (Map.Entry<String, Object> permRoles : urlPermRolesRules.entrySet()) {
            String perm = permRoles.getKey();
            if (pathMatcher.match(perm, restfulPath)) {
                List<String> roles = Convert.toList(String.class, permRoles.getValue());//["ADMIN",""]
                authorizedRolesCan.addAll(roles);
                requireCheck = true;
            }
        }

        if(requireCheck==false){
            //数据库中没设置规则的url都统一只认证。不鉴权
            return Mono.just(new AuthorizationDecision(true));
        }

        Mono<AuthorizationDecision> authorizationDecisionMono = mono.filter(Authentication::isAuthenticated)
                .flatMapIterable(Authentication::getAuthorities)
                .map(GrantedAuthority::getAuthority)//获取["ROLE_ADMIN","ROLE_XXX"]
                .any(authority->{
                    String roleCode = StrUtil.removePrefix(authority, AuthConstant.AUTHORITY_ROLE_PREFIX);
                    if("ROOT".equals(roleCode)){
                        //超级管理员
                        return true;
                    }
                    boolean hasAuthorized = CollectionUtil.isNotEmpty(authorizedRolesCan) && authorizedRolesCan.contains(roleCode);
                    return hasAuthorized;
                }).map(AuthorizationDecision::new).defaultIfEmpty(new AuthorizationDecision(false));

        return authorizationDecisionMono;
    }
}
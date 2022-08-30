package com.wm.jwtauth.filter;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.wm.core.model.vo.base.BaseResp;
import com.wm.core.model.vo.base.ResultCode;
import com.wm.jwtauth.constants.ConfigConst;
import com.wm.jwtauth.global.UserContext;
import com.wm.jwtauth.global.UserInfo;
import com.wm.jwtauth.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-08-30 10:49
 */
@Slf4j
public class JwtFilter extends GenericFilterBean {
    @Resource
    private JwtUtil jwtUtil;

    private static List<Pattern> patternList = new ArrayList<>();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException{
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;

        //如果是网关过来的无需拦截的url,直接通过
        String url = request.getHeader("no_auth_url");
        if(StringUtils.isNotBlank(url)){
            try {
                filterChain.doFilter(request,response);
                return;
            }catch (Exception e){
                responseUnAuthReturn(response);
            }
        }
        String token = request.getHeader("token");
        if(StringUtils.isBlank(token)){
            try {
                //如果不带token，默认通过。//TODO 查看是否在不许过滤的url列表中
                filterChain.doFilter(request,response);
                return;
            }catch (Exception e){
                responseUnAuthReturn(response);
            }
        }else{
            try {
                DecodedJWT jwt = jwtUtil.verifyToken(token);
                Map<String, Claim> claimMap = jwt.getClaims();
                Date now = new Date();
                if(now.after(jwt.getExpiresAt())){
                    responseUnAuthReturn(response);
                    return;
                }
                UserInfo userInfo = new UserInfo();
                userInfo.setUserId(claimMap.get(ConfigConst.auth.USER_ID).asString());
                userInfo.setUserName(claimMap.get(ConfigConst.auth.USER_NAME).asString());
                userInfo.setRoles(this.getRoleSetByString(claimMap.get(ConfigConst.auth.USER_ROLES).asString()));
                UserContext.getUserInfo().set(userInfo);
            }catch (Exception e){
                responseUnAuthReturn(response);
                return;
            }
            try {
                filterChain.doFilter(request,response);
            }catch (Exception e){
                responseUnAuthReturn(response);
            }finally {
                UserContext.getUserInfo().remove();
            }
        }
    }

    private Set<String> getRoleSetByString(String roles) {
        return new HashSet<>(Arrays.asList(roles.split(",")));
    }

    private boolean isInclude(String url) {
        if(url.contains("/swagger-ui.html")|| url.contains("/swagger-resources") || url.contains("/v2/api-docs")
                ||  url.contains("/webjars/")||url.contains("/doc.html")){
            return true;
        }
        for (Pattern pattern : patternList){
            Matcher matcher = pattern.matcher(url);
            if(matcher.matches()){
                return true;
            }
        }
        return false;
    }

    private void responseUnAuthReturn(HttpServletResponse response) throws IOException {
        BaseResp result = new BaseResp<>();
        result.setCode(ResultCode.TOKEN_INVALID_OR_EXPIRED.getCode());
        result.setDesc(ResultCode.TOKEN_INVALID_OR_EXPIRED.getMsg());
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(JSON.toJSONString(result));
    }

    private void responseRoleNotSupport(HttpServletResponse response) throws IOException {
        BaseResp result = new BaseResp<>();
        result.setCode(ResultCode.ACCESS_ROLE_UNAUTHORIZED.getCode());
        result.setDesc(ResultCode.ACCESS_ROLE_UNAUTHORIZED.getMsg());
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(JSON.toJSONString(result));
    }


    public static List<Pattern> getPatternList() {
        return patternList;
    }

}
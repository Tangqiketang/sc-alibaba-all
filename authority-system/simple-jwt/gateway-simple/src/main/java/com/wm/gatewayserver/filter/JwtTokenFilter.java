package com.wm.gatewayserver.filter;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.wm.core.model.vo.base.BaseResp;
import com.wm.core.model.vo.base.ResultCode;
import com.wm.gatewayserver.properties.IgnoreUrls;
import com.wm.gatewayserver.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-08-24 11:23
 */
@Component
@Slf4j
@RefreshScope
public class JwtTokenFilter implements GlobalFilter, Ordered, CommandLineRunner {

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private IgnoreUrls ignoreUrls;
    private static List<Pattern> patternList = new ArrayList<>();

    @Value("${token.need:true}")
    private Boolean tokenNeed;

    private static final String NO_AUTH_URL = "no_auth_url";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if(!tokenNeed){
            return chain.filter(exchange);
        }
        //清洗请求头中的no_auth_url,防止冒充
        ServerHttpRequest request = exchange.getRequest().mutate()
                .headers(httpHeaders -> httpHeaders.remove(NO_AUTH_URL)).build();

        String url = exchange.getRequest().getURI().getPath(); //serviceweb/xxx/xx
        if(isInclude(url)){
            exchange.getRequest().mutate().header(NO_AUTH_URL, "1");
            return chain.filter(exchange);
        }
        String token = exchange.getRequest().getHeaders().getFirst("token");
        if(RequestMethod.OPTIONS.name().equals(exchange.getRequest().getMethod())){
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.OK);
            return chain.filter(exchange);
        }else{
            if(StringUtils.isBlank(token)){
                return responseUnAuthReturn(exchange);
            }
            try {
                DecodedJWT jwt = jwtUtil.verifyToken(token);
                Date now = new Date();
                if(now.after(jwt.getExpiresAt())){
                    return responseTimeOutReturn(exchange);
                }
            }catch (Exception e){
                return responseUnAuthReturn(exchange);
            }
        }
        return chain.filter(exchange);
    }

    private Mono<Void> responseTimeOutReturn(ServerWebExchange exchange) {
        return getVoidMono(exchange);
    }
    private Mono<Void> responseUnAuthReturn(ServerWebExchange exchange) {
        return getVoidMono(exchange);
    }

    private Mono<Void> getVoidMono(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        BaseResp result = new BaseResp<>();
        result.setCode(ResultCode.TOKEN_INVALID_OR_EXPIRED.getCode());
        result.setDesc(ResultCode.TOKEN_INVALID_OR_EXPIRED.getMsg());
        response.getHeaders().add("Access-Control-Allow-Origin", "*");
        response.getHeaders().add("Content-Type","application/json; charset=utf-8");
        response.setStatusCode(HttpStatus.OK);
        byte[] responseByte = JSONObject.toJSON(result).toString().getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(responseByte);
        return  response.writeWith(Flux.just(buffer));
    }

    @Override
    public int getOrder() {
        return NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER +2;
    }

    private boolean isInclude(String url) {
        if(url.contains("/swagger-ui.html")|| url.contains("/swagger-resources")
                || url.contains("/v2/api-docs")||  url.contains("/webjars/")||url.contains("/doc.html")){
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

    //初始化将配置文件中忽略权限的url初始化
    @Override
    public void run(String... args) throws Exception {
        if(!CollectionUtils.isEmpty(ignoreUrls.getIgnoreUrls())){
            for(String ignoreUrl:ignoreUrls.getIgnoreUrls()){
                patternList.add(Pattern.compile(ignoreUrl));
            }
        }
    }
}
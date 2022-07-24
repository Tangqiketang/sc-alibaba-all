package com.wm.gatewayserver.security.filter;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.nimbusds.jose.JWSObject;
import com.wm.core.model.constants.AuthConstant;
import com.wm.core.model.vo.base.ResultCode;
import com.wm.gatewayserver.security.utils.ResponseUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;

/**
 * 描述:所有的url都会过这里。全局过滤器
 *
 * @auther WangMin
 * @create 2022-07-06 19:27
 */
@Component
@Slf4j
public class GatewaySecurityFilter implements GlobalFilter, Ordered {

    //能进这里，说明要么在忽略的url中。要么是token bearer加密是正确的
    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request =exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        //RequestPath requestPathAll = request.getPath();
        //String requestPath = request.getPath().pathWithinApplication().value();

        String token = request.getHeaders().getFirst(AuthConstant.AUTHORIZATION_KEY);
        if(StringUtils.isBlank(token)||!StringUtils.startsWithIgnoreCase(token,AuthConstant.JWT_PREFIX)){
            //比如ignore列表中不带token的都可以过。或者也可以过认证的url，Basic
            return chain.filter(exchange);
        }

        //获取真实的token。使用security自带的jwt解密工具解密
        token = StringUtils.replace(token,AuthConstant.JWT_PREFIX,"");
        String payload = StrUtil.toString(JWSObject.parse(token).getPayload());

        //判断是否在黑名单中 TODO
        JSONObject jsonObject = JSONObject.parseObject(payload);
        String jti = jsonObject.getString(AuthConstant.JWT_JTI);
        boolean isBlack=false;
        if (isBlack) {
            return ResponseUtils.writeErrorInfo(response, ResultCode.TOKEN_ACCESS_FORBIDDEN);
        }

        //成功则将jwt解密之后的信息放入header中,传递到后面微服务
        request = exchange.getRequest().mutate()
                                        .header(AuthConstant.JWT_PAYLOAD_KEY, URLEncoder.encode(payload,"UTF-8"))
                                        .build();
        exchange = exchange.mutate().request(request).build();
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
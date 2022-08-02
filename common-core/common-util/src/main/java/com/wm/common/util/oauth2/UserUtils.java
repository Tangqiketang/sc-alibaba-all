package com.wm.common.util.oauth2;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wm.core.model.constants.AuthConstant;
import lombok.SneakyThrows;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-08-01 14:05
 */
public class UserUtils {

    /**
     * 获取去经过网关解析的payload,并解码
     * @return
     */
    @SneakyThrows
    public static JSONObject getJwtPayload() {
        JSONObject jsonObject = null;
        String payload = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader(AuthConstant.JWT_PAYLOAD_KEY);
        if (StrUtil.isNotBlank(payload)) {
            jsonObject = JSONUtil.parseObj(URLDecoder.decode(payload, StandardCharsets.UTF_8.name()));
        }
        return jsonObject;
    }

    /**
     * 获取当前用户
     * @return
     */
    public static Integer getUserId() {
        Integer userId = null;
        JSONObject jwtPayload = getJwtPayload();
        if (jwtPayload != null) {
            userId = jwtPayload.getInt("userId");
        }
        return userId;
    }

    /**
     * 获取当前用户角色集
     * @return
     */
    public static List<String> getRoles() {
        List<String> roles;
        JSONObject payload =  getJwtPayload();
        if (payload.containsKey(AuthConstant.JWT_AUTHORITIES_KEY)) {
            roles = payload.getJSONArray(AuthConstant.JWT_AUTHORITIES_KEY).toList(String.class);
        } else {
            roles = Collections.emptyList();
        }
        return roles;
    }


    public static boolean isRoot() {
        List<String> roles = getRoles();
        return CollectionUtil.isNotEmpty(roles) && roles.contains("ROOT");
    }
}
package com.wm.jwtauth.global;

import java.util.Optional;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-08-30 13:52
 */
public class UserContext {

    private static final ThreadLocal<UserInfo> userInfo = new  ThreadLocal<>();

    public static ThreadLocal<UserInfo> getUserInfo() {
        return userInfo;
    }

    public static String getUserName(){
        return  Optional.ofNullable(getUserInfo().get()).map(UserInfo::getUserName).orElse(null);
    }

}
package com.wm.jwtauth.global;

import lombok.Data;

import java.util.Set;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-08-30 13:53
 */
@Data
public class UserInfo {

    private String userId;

    private String userName;

    private Set<String> roles;
}
package com.wm.optional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Optional;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-07-05 10:03
 */
public class Test {

    public static void main(String[] args) {
        JSONObject jsonObject = null;
        Optional result1 = Optional.ofNullable(jsonObject).map(a->a.getJSONObject("params").getString("name"));

        String result2 = Optional.ofNullable(jsonObject).map(a->a.getJSONObject("params").getString("name"))
                         .orElse("");

        JSONArray result3 = Optional.ofNullable(jsonObject).map(a->a.getJSONObject("params").getJSONArray("deviceList"))
                .orElse(new JSONArray());

        System.out.println(result1); //Optional.empty
        System.out.println(result2); //
        System.out.println(result3); //[]


    }

}

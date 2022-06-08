package com.wm.common.util;

import cn.hutool.core.util.IdcardUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 描述:
 * 校验工具类
 *
 * @auther WangMin
 * @create 2022-06-08 16:30
 */
public class ValidateKit {


    /**
     * 校验是不是电话
     * @param phone
     * @return
     */
    public static boolean validatePhone(String phone) {
        String regex = "^(?:(?:\\+|00)86)?1(?:(?:3[\\d])|(?:4[5-7|9])|(?:5[0-3|5-9])|(?:6[5-7])|(?:7[0-8])|(?:8[\\d])|(?:9[1|8|9]))\\d{8}$";
        if(phone.length() != 11){
            return false;
        }else{
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phone);
            boolean isMatch = m.matches();
            if(isMatch){
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 校验是不是数字
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){
        if (StringUtils.isNotBlank(str)){
            Pattern pattern = Pattern.compile("[0-9]*");
            Matcher isNum = pattern.matcher(str);
            if( !isNum.matches() ){
                return false;
            }
            return true;
        }else{
            return false;
        }
    }


    /**
     * 校验是否身份证 hutool-all工具类
     * @param idCard
     * @return
     */
    public static boolean isIDCard(String idCard){
       return IdcardUtil.isValidCard(idCard);
    }

}

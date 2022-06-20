package com.wm.common.util;

import cn.hutool.core.util.StrUtil;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 描述: https://www.jianshu.com/p/1c49cfa72f94
 *
 * @auther WangMin
 * @create 2022-06-07 16:24
 */
public class StringKit {

    /********************** guava工具 **********************************************/

    /**
     *  使用特殊分隔符拼接
     * @param split  #
     * @param list {"a",null,"","b"}
     * @return a#b
     */
    public static String joinerOn(String split, List<Object> list){
        return Joiner.on(split).skipNulls().join(list);
    }

    /**
     * map按分隔符拼接字符串
     * @param split1 &
     * @param split2 =
     * @param map {"name":"jack","age":12}
     * @return name=jack&age=12
     */
    public static String joinerOnMap(String split1, String split2, Map<String,Object> map){
        return Joiner.on(split1).withKeyValueSeparator(split2).join(map);
    }

    /**
     * 按符号分割成list
     * @param split "|" guava不用转义
     * @param source Hello|Java
     * @return [Hello, Java]
     * @describe 去除空格
     */
    public static List<String> splitterOn2List(String split,String source){
        return Splitter.on(split).omitEmptyStrings().splitToList(source);
    }

    /**
     * 按符合多次分割成map,比如url后面的参数
     * @param split1 &
     * @param split2 =
     * @param source id=123&name=green
     * @return
     */
    public static Map<String,String> splitterOn2Map(String split1,String split2,String source){
        return Splitter.on(split1).withKeyValueSeparator(split2).split(source);
    }


    /**
     * 左侧补齐
     * @param s      bik
     * @param total  5 总长度
     * @param padStr a 用该字符补齐
     * @return       aa+bik
     */
    public static String leftPad(String s,int total,String padStr){
        return StringUtils.leftPad(s, total, padStr);
    }


    /**
     * 将字符串前缀删除
     * @param source  ROLE_ADMIN
     * @param rmKey ROLE_
     * @return  ADMIN
     */
    public static String removePrefix(String source,String rmKey){
        return StrUtil.removePrefix(source,rmKey);
    }



}

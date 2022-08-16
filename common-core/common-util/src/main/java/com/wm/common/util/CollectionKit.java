package com.wm.common.util;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-06-30 22:43
 */
public class CollectionKit {

    /**
     * 返回交集。取2个集合中，相同的部分。不影响原来的集合内容
     * @param list2
     * @return
     */
    public static List<String> retainAll(List<String> list1,List<String> list2){
        List<String> result = new ArrayList<>(list1);
        result.retainAll(list2);
        return result;

    }

    /**
     * 把list1中去掉list2,不影响原来的集合内容
     * @param list1 [1,2,3]
     * @param list2 [1,5]
     * @return [2,3]    (23要删的| 1 |5要插入的)
     */
    public static List<String> remove(List<String> list1,List<String> list2){
        List<String> result = new ArrayList<>(list1);
        if(!CollectionUtils.isEmpty(list2)){
            result.remove(list2);
        }
        return result;
    }


    /**
     * 复制集合。(浅拷贝,新集合,元素)
     * @param source
     * @param <T>
     * @return
     */
    public static <T> List<T> newCopy(List<T> source){
        return new ArrayList<>(source);
    }



}
package com.wm.common.util;

import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-06-30 22:43
 */
public class CollectionKit {


    /**
     *  Array 中的元素都添加到 List/Set 中
     * @param array
     * @param collection
     * @param <E>
     */
   public static <E> void mergeArrayIntoCollection(Object array, Collection<E> collection){
        CollectionUtils.mergeArrayIntoCollection(array,collection);
   }

    /**
     * 返回list中最后一个元素
     * @param list
     * @param <T>
     * @return
     */
   public static <T> T lastElement(List<T> list){
       return CollectionUtils.lastElement(list);
   }


}
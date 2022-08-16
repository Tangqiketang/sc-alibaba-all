package com.wm.common.util;

import com.alibaba.fastjson.JSON;
import com.rits.cloning.Cloner;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-08-05 18:41
 */
public class BeanKit {

    private static final Cloner cloner = new Cloner();

    /**
     * 复制对象（深度拷贝）
     * @param object
     * @param <T>
     * @return
     */
    public static <T> T clone(final T object){
        if (object == null) {
            return null;
        }
        return cloner.deepClone(object);
    }

    /**
     * 复制集合（深度拷贝）
     * @param object
     * @param <T>
     * @return
     */
    public static <T> List<T> cloneList(final List<T> object){
        if (object == null) {
            return null;
        }
        return cloner.deepClone(object);
    }

    public static<T> T deepClone(T src) throws IOException, ClassNotFoundException {
        Object obj = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(src);
        objectOutputStream.close();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        obj = objectInputStream.readObject();
        objectInputStream.close();
        return (T) obj;
    }


    /**
     * 复制对象到指定类（深度拷贝）
     * @param object
     * @param destclas 指定类
     * @param <T>
     * @return
     */
    public static <T> T clone(final Object object, Class<T> destclas){
        if (object == null) {
            return null;
        }
        String json = JSON.toJSONString(object);
        return JSON.parseObject(json, destclas);
    }

    /**
     * 复制集合到指定类（深度拷贝）
     * @param object
     * @param destclas 指定类
     * @param <T>
     * @return
     */
    public static <T> List<T> cloneList(List<?> object, Class<T> destclas) {
        if (object == null) {
            return new ArrayList<T>();
        }
        String json = JSON.toJSONString(object);
        return JSON.parseArray(json, destclas);
    }

}
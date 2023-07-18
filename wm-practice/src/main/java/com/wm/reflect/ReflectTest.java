package com.wm.reflect;

import com.wm.reflect.handler.ParkingInOutEventHandler;
import com.wm.reflect.listener.EventListener;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ReflectTest {


       public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class aClass = ParkingInOutEventHandler.class;
        Constructor c1 = aClass.getDeclaredConstructor();
        c1.setAccessible(true);
        EventListener biListener = (EventListener) c1.newInstance();
        Type[] genericInterfaces = aClass.getGenericInterfaces();
        Type[] events;
        if (genericInterfaces.length == 0) {
            events = biListener.eventTypes();
        } else {
            //  这里强转是因为 ParameterizedType 继承Type接口 并可以获取对应的参数类
            ParameterizedType genericInterface = (ParameterizedType) genericInterfaces[0];
            events = genericInterface.getActualTypeArguments();
        }
    }
}

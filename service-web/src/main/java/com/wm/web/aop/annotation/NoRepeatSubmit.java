package com.wm.web.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自动幂等注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoRepeatSubmit {

    /*
     * 持有时间
     * 设置请求锁定时间。leaseTime为-1，则保持锁定直到显式解锁
     * @return
     */
    long leaseTime() default 10;

    //获取锁的最大尝试时间.超过则请求返回失败
    long waitTIme() default 5;


}

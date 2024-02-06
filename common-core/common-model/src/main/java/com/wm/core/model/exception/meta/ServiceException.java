package com.wm.core.model.exception.meta;

import java.io.Serializable;

/**
 * @Description:系统异常统一抛出处理
 * @Auther: wangmin
 * @Date: 9:13 2020/7/8
 */
public class ServiceException extends RuntimeException implements Serializable{

    private String code;
    private String message;


    /**
     * 不写入堆栈信息，提高性能
     */
    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

    public ServiceException(String code,String message){
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

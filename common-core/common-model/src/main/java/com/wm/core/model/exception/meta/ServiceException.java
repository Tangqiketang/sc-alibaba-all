package com.wm.core.model.exception.meta;

/**
 * @Description:系统异常统一抛出处理
 * @Auther: wangmin
 * @Date: 9:13 2020/7/8
 */
public class ServiceException extends RuntimeException {
    private ExceptionEnum exceptionEnum;

    public ServiceException(ExceptionEnum exceptionEnum) {
        this.exceptionEnum = exceptionEnum;
    }

    public ExceptionEnum getExceptionEnum() {
        return exceptionEnum;
    }

    public void setExceptionEnum(ExceptionEnum exceptionEnum) {
        this.exceptionEnum = exceptionEnum;
    }

    public ServiceException(String code,String message){
        this.exceptionEnum = new ExceptionEnum() {
            @Override
            public String getCode() {
                return code;
            }
            @Override
            public String getMessage() {
                return message;
            }
        };
    }

}

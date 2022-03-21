package com.wm.logging.anotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogPrint {

    /**
     * 日志内容
     * @return {String}
     */
    String value();

}

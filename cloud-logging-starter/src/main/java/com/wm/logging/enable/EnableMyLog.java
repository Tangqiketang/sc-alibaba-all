package com.wm.logging.enable;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 在引用这个日志的项目中，注入该注解，就会导入Bean，从而使config/AutoLogConfig生效
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(LogMarkerConfiguration.class) //往容器中注入xxBean
public @interface EnableMyLog {
}

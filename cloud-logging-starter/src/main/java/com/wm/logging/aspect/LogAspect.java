package com.wm.logging.aspect;

import com.wm.logging.anotation.LogPrint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * 描述:
 * 对注解打日志
 *
 * @auther WangMin
 * @create 2022-03-21 16:10
 */
@Aspect
public class LogAspect {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**对有注解的方法打印日志 **/
    @Pointcut("@annotation(com.wm.logging.anotation.LogPrint)")
    public void logPointCut() {

    }
    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        //类名
        String className = pjp.getTarget().getClass().getName();
        //方法名
        String methodName = signature.getName();

        LogPrint syslog = method.getAnnotation(LogPrint.class);
        //操作
        String operator =syslog.value();

        long beginTime = System.currentTimeMillis();

        Object returnValue = null;
        Exception ex = null;
        try {
            returnValue = pjp.proceed();
            return returnValue;
        } catch (Exception e) {
            ex = e;
            throw e;
        } finally {
            long cost = System.currentTimeMillis() - beginTime;
            if (ex != null) {
                log.error("[class: {}][method: {}][operator: {}][cost: {}ms][args: {}][发生异常]",
                        className, methodName, operator, pjp.getArgs(), ex);
            } else {
                log.info("[class: {}][method: {}][operator: {}][cost: {}ms][args: {}][return: {}]",
                        className, methodName, operator, cost, pjp.getArgs(), returnValue);
            }
        }

    }


}

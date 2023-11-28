package com.wm.web.aop.aspect;

import com.alibaba.fastjson.JSON;
import com.wm.core.model.exception.meta.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 调用其他服务打印日志
 * TODO
 *
 */
@Aspect
@Component
@Slf4j
public class ApiClientAspect {

    @Pointcut("execution(public * com.wm.web.service.impl.IpcCameraServiceImpl.*(..))")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        try {
            Object result = joinPoint.proceed();
            log.info("xiaoniao平台调用参数为:{},结果为:{}", JSON.toJSONString(args),JSON.toJSONString(result));
            return result;
        } catch (Throwable e) {
            log.info("xiaoniao平台调用参数为:{},异常为:{}", JSON.toJSONString(args),e.getMessage());
            throw new ServiceException("500", "小鸟平台请求异常");
        }
    }

}

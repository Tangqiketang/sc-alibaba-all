package com.wm.web.aop.log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wm.core.model.exception.meta.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 描述: controller
 *
 * @auther WangMin
 * @create 2022-12-16 14:20
 */
@Aspect
@Component
@Slf4j
public class WebLogAspect {

    /**
     * 进入方法时间戳
     */
    private Long startTime;
    /**
     * 方法结束时间戳(计时)
     */
    private Long endTime;

    public WebLogAspect() {
    }


    /**
     * 定义请求日志切入点，其切入点表达式有多种匹配方式,这里是指定路径
     */
    @Pointcut("execution(public * com.wm.web.controller.*.*(..))")
    public void webLogPointcut() {
    }

    /**
     * 前置通知：
     * 1. 在执行目标方法之前执行，比如请求接口之前的登录验证;
     * 2. 在前置通知中设置请求日志信息，如开始时间，请求参数，注解内容等
     *
     * @param joinPoint
     * @throws Throwable
     */
/*    @Before("webLogPointcut()")
    public void doBefore(JoinPoint joinPoint) {

        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        startTime = System.currentTimeMillis();
        String[] parameterNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();

        Object[] args = joinPoint.getArgs();
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < args.length; i++) {
            JSONObject jsonObject = new JSONObject();
            //过滤入参为MultipartFile，Request, Response
            if (args[i] instanceof MultipartFile) {
                jsonObject.put(parameterNames[i], "Object[MultipartFile]");
            } else if (args[i] instanceof HttpServletRequest) {
                jsonObject.put(parameterNames[i], "Object[HttpServletRequest]");
            } else if (args[i] instanceof HttpServletResponse) {
                jsonObject.put(parameterNames[i], "Object[HttpServletResponse]");
            } else if (isBasicType(args[i])) {
                jsonObject.put(parameterNames[i], args[i]);
            } else if (args[i] instanceof Collection) {
                //集合类型，List，Map，Set
                jsonObject.put(parameterNames[i], args[i]);
            }else {
                //自定义对象
                jsonObject = (JSONObject) JSON.toJSON(args[i]);
            }
            list.add(jsonObject);
        }

        log.debug("请求方式：{},请求Url : {},请求参数:{}",request.getMethod(),request.getRequestURL().toString(), JSON.toJSONString(list));
    }*/


    @Around("webLogPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) {

        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        startTime = System.currentTimeMillis();
        String[] parameterNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();

        Object[] args = joinPoint.getArgs();
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < args.length; i++) {
            JSONObject jsonObject = new JSONObject();
            //过滤入参为MultipartFile，Request, Response
            if (args[i] instanceof MultipartFile) {
                jsonObject.put(parameterNames[i], "Object[MultipartFile]");
            } else if (args[i] instanceof HttpServletRequest) {
                jsonObject.put(parameterNames[i], "Object[HttpServletRequest]");
            } else if (args[i] instanceof HttpServletResponse) {
                jsonObject.put(parameterNames[i], "Object[HttpServletResponse]");
            } else if (isBasicType(args[i])) {
                jsonObject.put(parameterNames[i], args[i]);
            } else if (args[i] instanceof Collection) {
                //集合类型，List，Map，Set
                jsonObject.put(parameterNames[i], args[i]);
            }else {
                //自定义对象
                jsonObject = (JSONObject) JSON.toJSON(args[i]);
            }
            list.add(jsonObject);
        }
        try{
            Object result =joinPoint.proceed();
            log.debug("小鸟宇泛请求方式:{},请求Url:{},请求参数:{},响应参数:{}",request.getMethod(),request.getRequestURL().toString(), JSON.toJSONString(list),JSON.toJSONString(result));
            return result;
        }catch (Throwable e){
            log.error("小鸟宇泛请求方式:{},请求Url:{},请求参数:{}",request.getMethod(),request.getRequestURL().toString(), JSON.toJSONString(list),e);
            throw new ServiceException("500","aop异常");
        }



    }



    private boolean isBasicType(Object value) {
        if (value == null) {
            return false;
        } else if (ClassUtils.isPrimitiveOrWrapper(value.getClass())) {
            return true;
        } else if (value instanceof String) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 返回通知：
     * 1. 在目标方法正常结束之后执行
     * 1. 在返回通知中补充请求日志信息，如返回时间，方法耗时，返回值，并且保存日志信息
     *
     * @param ret
     * @throws Throwable
     */
/*    @AfterReturning(returning = "ret", pointcut = "webLogPointcut()")
    public void doAfterReturning(Object ret) throws Throwable {
        endTime = System.currentTimeMillis();
        //log.debug("请求结束时间：{}" + LocalDateTime.now());
      //  log.debug("请求耗时：{}" + (endTime - startTime));
        // 处理完请求，返回内容
        log.debug("请求返回 : {}" + ret);
    }*/

    /**
     * 异常通知：
     * 1. 在目标方法非正常结束，发生异常或者抛出异常时执行
     * 1. 在异常通知中设置异常信息，并将其保存
     *
     * @param throwable
     */
/*    @AfterThrowing(value = "webLogPointcut()", throwing = "throwable")
    public void doAfterThrowing(Throwable throwable) {
        // 保存异常日志记录
       // log.error("发生异常时间：{}" + LocalDateTime.now());
       // log.error("抛出异常：{}" + throwable.getMessage());
    }*/

}

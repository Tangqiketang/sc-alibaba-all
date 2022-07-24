package com.wm.common.config;


import com.wm.core.model.exception.meta.ServiceException;
import com.wm.core.model.exception.meta.SysExceptionEnum;
import com.wm.core.model.vo.base.BaseResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.ResourceUrlProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Set;
import java.util.StringJoiner;


@ControllerAdvice
@Slf4j
public class GlobalExceptionController {

	@Autowired
	private ResourceUrlProvider resourceUrlProvider;

	/**
	 * 处理静态资源的版本
	 *
	 * @return
	 */
	@ModelAttribute("urls")
	public ResourceUrlProvider urls() {
		return this.resourceUrlProvider;
	}

	/**
	 * 统一contextPath输出
	 *
	 * @param request
	 * @return
	 */
	@ModelAttribute("contextPath")
	public String contextPath(HttpServletRequest request) {
		return request.getContextPath();
	}



	/**
	 * 自定义错误，前台传入参数有误
	 * @param req
	 * @param ex
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@ExceptionHandler(ServiceException.class)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public BaseResp handleReqException(HttpServletRequest req, ServiceException ex, HttpServletResponse response) throws UnsupportedEncodingException{
		BaseResp rsp = new BaseResp();
		rsp.setCode(ex.getExceptionEnum().getCode());
		rsp.setDesc(ex.getExceptionEnum().getMessage());
		return rsp;
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public BaseResp handleException(HttpServletRequest req, Exception e){
		BaseResp rsp = new BaseResp();
		if (e instanceof MissingServletRequestParameterException) {
			rsp.setCode(SysExceptionEnum.BAD_REQUEST_PARAM_MISS.getCode());
			rsp.setDesc(SysExceptionEnum.BAD_REQUEST_PARAM_MISS.getMessage());
		} else if (e instanceof MethodArgumentTypeMismatchException
				|| ((e instanceof IllegalStateException) && e.getMessage().contains("parameter"))) {
			rsp.setCode(SysExceptionEnum.BAD_REQUEST_PARAM_ERROR.getCode());
			rsp.setDesc(SysExceptionEnum.BAD_REQUEST_PARAM_ERROR.getMessage());
		} else if (e instanceof BindException) {
			if (((BindException) e).hasErrors()) {
				StringBuilder errors = new StringBuilder();
				for (FieldError fieldError : ((BindException) e).getBindingResult().getFieldErrors()) {
					errors.append(fieldError.getDefaultMessage()).append("，");
				}
				rsp.setCode(SysExceptionEnum.BAD_REQUEST_PARAM_ERROR.getCode());
				rsp.setDesc(SysExceptionEnum.BAD_REQUEST_PARAM_ERROR.getMessage() + " -> " + errors.substring(0, errors.lastIndexOf("，")));
			}
		} else if (e instanceof ConstraintViolationException) {
			Set<ConstraintViolation<?>> set = ((ConstraintViolationException) e).getConstraintViolations();
			if (set != null && !set.isEmpty()) {
				Iterator<ConstraintViolation<?>> iterator = set.iterator();
				StringJoiner joiner = new StringJoiner("，", "", "");
				while (iterator.hasNext()) {
					joiner.add(iterator.next().getMessage());
				}
				rsp.setCode(SysExceptionEnum.BAD_REQUEST_PARAM_ERROR.getCode());
				rsp.setDesc(SysExceptionEnum.BAD_REQUEST_PARAM_ERROR.getMessage() + " -> " + joiner.toString());
			}
		}else if(e instanceof IllegalArgumentException){
			rsp.setCode(SysExceptionEnum.BAD_REQUEST_PARAM_ERROR.getCode());
			rsp.setDesc(SysExceptionEnum.BAD_REQUEST_PARAM_ERROR.getMessage());
		} else {
			log.error("[{}]_{}", SysExceptionEnum.SYSTEM_ERROR.getCode(), e.getMessage(), e);
			rsp.setCode(SysExceptionEnum.SYSTEM_ERROR.getCode());
			rsp.setDesc(SysExceptionEnum.SYSTEM_ERROR.getMessage());
		}
		return rsp;
		
	}

	

}

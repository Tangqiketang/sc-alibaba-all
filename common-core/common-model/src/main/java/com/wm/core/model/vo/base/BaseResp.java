package com.wm.core.model.vo.base;

import com.alibaba.fastjson.JSONObject;
import com.wm.core.model.exception.meta.ExceptionEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value="接口返回对象", description="接口返回对象")
@Data
@Builder
public class BaseResp<T> implements Serializable {

    public String code;
    public String desc;

	@ApiModelProperty(value = "返回数据对象")
    public T result;

    public BaseResp() {
        super();
    }

    public static <T> BaseResp<T> ok(T t){
		BaseResp<T> result = new BaseResp<T>();
		result.setResult(t);
		result.setCode("0");
		return result;
    }

	public static <T> BaseResp<T> error(ResultCode resultCode){
		BaseResp<T> result = new BaseResp<T>();
		result.setCode(resultCode.getCode());
		result.setDesc(resultCode.getMsg());
		return result;
	}


    public BaseResp(String code, String desc) {
        super();
        this.code = code;
        this.desc = desc;
    }


	public BaseResp(String code, String desc, T result) {
		super();
		this.code = code;
		this.desc = desc;
		this.result = result;
	}

	public BaseResp(ExceptionEnum exEnum){
    	this.code = exEnum.getCode();
    	this.desc = exEnum.getMessage();
	}


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}


	@Override
	public String toString() {
    	return JSONObject.toJSONString(this);
	}

	public static BaseResp returnSuccess(String code,String desc,Object result){
		BaseResp rsp = new BaseResp();
		rsp.setCode(code);
		rsp.setDesc(desc);
		rsp.setResult(result);
		return rsp;
	}


}

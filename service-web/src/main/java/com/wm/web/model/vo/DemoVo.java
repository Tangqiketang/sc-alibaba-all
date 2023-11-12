package com.wm.web.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-06-15 16:03
 */
@ApiModel(value="Demo", description="DemoVo")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Data
public class DemoVo {

    @ApiModelProperty(value = "名称")
    private String name;


    @ApiModelProperty(value = "integer")
    private Integer age;

    //json转换时指定字段映射
    @JsonProperty("my_xxxxxxxxxxxxxxxxxcompany")
    private String cameraCompany;

    @ApiModelProperty(value = "订单创建时间")
    //jsonFormat用于json序列化时
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    //DateFormat是spring框架，一般用于表单提交
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;


    @ApiModelProperty(value = "任务开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone= "GMT+8")
    @JsonSerialize(using = LocalDateSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @ApiModelProperty(value = "任务结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone= "GMT+8")
    @JsonSerialize(using = LocalDateSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
}

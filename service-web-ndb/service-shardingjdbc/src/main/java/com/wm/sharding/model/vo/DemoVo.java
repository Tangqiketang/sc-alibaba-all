package com.wm.sharding.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@ApiModel(value="DemoVo", description="DemoVo")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Data
public class DemoVo {

    @ApiModelProperty(value = "名称")
    private String name;


    @ApiModelProperty(value = "integer")
    private Integer age;



    @ApiModelProperty(value = "订单创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
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

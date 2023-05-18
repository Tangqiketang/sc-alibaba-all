package com.wm.mongo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 描述: mongo
 *
 * @auther WangMin
 * @create 2023-01-10 11:16
 */
@Document(collection  = "user")
@Data
public class User implements Serializable {

    @ApiModelProperty(value = "自动生成主键")
    @Id
    private String id;

    //@Indexed
    @ApiModelProperty(value = "用户名")
    @Field("username")
    private String username;

    @ApiModelProperty(value = "年龄")
    private Integer age;

    @ApiModelProperty(value = "性别")
    private Integer gender;

    @ApiModelProperty(value = "经度")
    private Double lng;

    @ApiModelProperty(value = "纬度")
    private Double lat;

    @ApiModelProperty(value = "高度")
    private Double height;

    //@Indexed
    @ApiModelProperty("上报时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date reportTimestamp;



}
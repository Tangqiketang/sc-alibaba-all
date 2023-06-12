package com.wm.auth.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 微信用户表
 * </p>
 *
 * @author Wang Min
 * @since 2022-09-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SysWxUser对象", description="微信用户表")
public class SysWxUser extends Model<SysWxUser> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    @ApiModelProperty(value = "微信用户昵称")
    private String nickName;

    @ApiModelProperty(value = "微信openid")
    private String openId;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "微信头像")
    private String avatarUrl;

    private Boolean gender;

    @ApiModelProperty(value = "用户状态（0：用户被禁用，1：用户正常）")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

}

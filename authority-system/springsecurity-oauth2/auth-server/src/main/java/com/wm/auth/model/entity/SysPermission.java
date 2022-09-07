package com.wm.auth.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 权限表。注:不在表里面的url只做认证,不做权限控制
 * </p>
 *
 * @author Wang Min
 * @since 2022-07-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SysPermission对象", description="权限表。注:不在表里面的url只做认证,不做权限控制")
public class SysPermission extends Model<SysPermission> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "权限按钮名称")
    private String name;

    @ApiModelProperty(value = "所属菜单模块id")
    private Integer menuId;

    @ApiModelProperty(value = "按钮权限url路径")
    private String urlPerm;

    @ApiModelProperty(value = "按钮权限标识用于前端")
    private String btnPerm;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}

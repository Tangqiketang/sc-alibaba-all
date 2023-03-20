package com.wm.web.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * t_device_prop_history
 * </p>
 *
 * @author Wang Min
 * @since 2023-03-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("device_log.t_device_prop_history")
@ApiModel(value = "DevicePropHistory对象", description = "t_device_prop_history")
public class DevicePropHistory extends Model {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "入库时间")
    @TableField("in_time")
    private LocalDateTime inTime;

    @ApiModelProperty(value = "发生时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "产品编号")
    private String productKey;

    @ApiModelProperty(value = "设备编号")
    private String deviceName;

    @ApiModelProperty(value = "属性")
    @TableField("prop_value")
    private String propValue;

    @ApiModelProperty(value = "值")
    private String identifier;


}

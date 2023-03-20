package com.wm.web.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * 相机
 * </p>
 *
 * @author Wang Min
 * @since 2023-03-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ipc_camera")
@ApiModel(value = "IpcCamera对象", description = "相机")
public class IpcCamera extends Model {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("camera_status")
    private Integer cameraStatus;

    @ApiModelProperty(value = "相机名称")
    @TableField("camera_name")
    private String cameraName;

    @TableField("camera_company")
    private String cameraCompany;

    @TableField("camera_model_type")
    private String cameraModelType;

    @TableField("camera_url")
    private String cameraUrl;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;


}

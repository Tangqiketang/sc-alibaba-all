package com.wm.web.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * <p>
 * 
 * </p>
 *
 * @author Wang Min
 * @since 2023-07-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "public.test_user",autoResultMap = true)
@ApiModel(value = "TestUser对象", description = "")
public class TestUser extends Model {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "名字")
    private String name;

    private Integer age;


    /**1.@TableName autoResultMap=true
     * 2.typeHandler
     * 3.mapper.xml里面类型填写**/
    @ApiModelProperty(value = "家庭关系")
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private Family family;

    @TableField("create_time")
    private LocalDate createTime;

    @Data
    public static class Family{
        private String name;
        private Integer age;
        private String nick;
    }

}
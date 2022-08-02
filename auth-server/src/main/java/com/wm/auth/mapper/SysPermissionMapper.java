package com.wm.auth.mapper;

import com.wm.auth.model.entity.SysPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 权限表。注:不在表里面的url只做认证,不做权限控制 Mapper 接口
 * </p>
 *
 * @author Wang Min
 * @since 2022-07-31
 */
public interface SysPermissionMapper extends BaseMapper<SysPermission> {

}

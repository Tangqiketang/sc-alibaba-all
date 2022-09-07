package com.wm.auth.service;

import com.wm.auth.model.entity.SysPermission;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 权限表。注:不在表里面的url只做认证,不做权限控制 服务类
 * </p>
 *
 * @author Wang Min
 * @since 2022-07-31
 */
public interface ISysPermissionService extends IService<SysPermission> {

}

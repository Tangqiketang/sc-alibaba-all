package com.wm.auth.service.impl;

import com.wm.auth.model.entity.SysPermission;
import com.wm.auth.mapper.SysPermissionMapper;
import com.wm.auth.service.ISysPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 权限表。注:不在表里面的url只做认证,不做权限控制 服务实现类
 * </p>
 *
 * @author Wang Min
 * @since 2022-07-31
 */
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements ISysPermissionService {

}

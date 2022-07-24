package com.wm.auth.service.impl;

import com.wm.auth.model.entity.Role;
import com.wm.auth.mapper.RoleMapper;
import com.wm.auth.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author Wang Min
 * @since 2022-07-19
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

}

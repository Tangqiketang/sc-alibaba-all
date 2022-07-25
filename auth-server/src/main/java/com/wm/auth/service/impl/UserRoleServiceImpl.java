package com.wm.auth.service.impl;

import com.wm.auth.model.entity.UserRole;
import com.wm.auth.mapper.UserRoleMapper;
import com.wm.auth.service.IUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户和角色关联表 服务实现类
 * </p>
 *
 * @author Wang Min
 * @since 2022-07-19
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

}

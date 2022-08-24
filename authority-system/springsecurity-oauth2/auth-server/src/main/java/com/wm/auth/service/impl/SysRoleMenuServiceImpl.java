package com.wm.auth.service.impl;

import com.wm.auth.model.entity.SysRoleMenu;
import com.wm.auth.mapper.SysRoleMenuMapper;
import com.wm.auth.service.ISysRoleMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色和菜单关联表 服务实现类
 * </p>
 *
 * @author Wang Min
 * @since 2022-07-31
 */
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements ISysRoleMenuService {

}

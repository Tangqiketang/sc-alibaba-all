package com.wm.auth.mapper;

import com.wm.auth.model.entity.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Set;

/**
 * <p>
 * 菜单管理 Mapper 接口
 * </p>
 *
 * @author Wang Min
 * @since 2022-07-31
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    Set<SysMenu> findMenuByRoleId(String roleCode);
}

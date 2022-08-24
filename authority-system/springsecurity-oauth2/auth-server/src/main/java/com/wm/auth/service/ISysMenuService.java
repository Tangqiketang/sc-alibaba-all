package com.wm.auth.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wm.auth.model.entity.SysMenu;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 菜单管理 服务类
 * </p>
 *
 * @author Wang Min
 * @since 2022-07-31
 */
public interface ISysMenuService extends IService<SysMenu> {

    List<Tree<Long>> getCurrentUserMenu(List<String> roles, Long parentId);

    Set<SysMenu> findMenuByRoleId(String roleCode);

    List<Tree<Long>> getMenuTree(boolean lazy, Long parentId);
}

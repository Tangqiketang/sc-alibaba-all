package com.wm.auth.service.impl;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wm.auth.mapper.SysMenuMapper;
import com.wm.auth.model.entity.SysMenu;
import com.wm.auth.service.ISysMenuService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单管理 服务实现类
 * </p>
 *
 * @author Wang Min
 * @since 2022-07-31
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    @Override
    public List<Tree<Long>> getCurrentUserMenu(List<String> roles, Long parentId) {
        Set<SysMenu> ownMenuSet = new HashSet<>();
        if(roles.contains("ROOT")){
            ownMenuSet = (Set<SysMenu>) this.baseMapper.selectList(new LambdaQueryWrapper<SysMenu>());
        }else{
            ownMenuSet = roles.stream().map(roleCode->findMenuByRoleId(roleCode))
                    .flatMap(Collection::stream).collect(Collectors.toSet());
        }
        //把数据库中菜单放入Tree工具类,自动生成树形结构
        List<Tree<Long>> treeList = filterMenu(ownMenuSet,parentId);
        return treeList;
    }

    /**
     * 自动形成parentId为root的树
     * @param menuSet
     * @param parentId
     * @return
     */
    private List<Tree<Long>> filterMenu(Set<SysMenu> menuSet, Long parentId) {
        List<TreeNode<Long>> allMenuTreeNode = menuSet.stream().map(menu-> getNodeFunction(menu)).collect(Collectors.toList());
        parentId = parentId==null?0:parentId;
        return TreeUtil.build(allMenuTreeNode,parentId);
    }

    private TreeNode<Long> getNodeFunction(SysMenu menu) {
        TreeNode<Long> node = new TreeNode<>();
        node.setId(Long.valueOf(menu.getId()));
        node.setName(menu.getName());
        node.setParentId(Long.valueOf(menu.getParentId()));
        node.setWeight(menu.getSort());
        //扩展属性
        Map<String,Object> extra = new HashMap<String,Object>();
        extra.put("menuType",menu.getMenuType());
        extra.put("path",menu.getPath());
        extra.put("component",menu.getComponent());
        extra.put("icon",menu.getIcon());
        extra.put("redirect",menu.getRedirect());
        extra.put("description",menu.getDescription());
        node.setExtra(extra);
        return node;
    }

    @Override
    @Cacheable(cacheNames = "menu", key = "#roleCode  + '_menu'", unless = "#result == null")
    public Set<SysMenu> findMenuByRoleId(String roleCode) {
        return baseMapper.findMenuByRoleId(roleCode);
    }

    @Override
    public List<Tree<Long>> getMenuTree(boolean lazy, Long parentId) {
        if(lazy){
            Long parent = parentId == null ? 0 : parentId;
            Set<SysMenu> menuSet = (Set<SysMenu>) this.baseMapper.selectList(Wrappers.<SysMenu>lambdaQuery().eq(SysMenu::getParentId, parent).orderByAsc(SysMenu::getSort));
            List<TreeNode<Long>> allMenuTreeNode = menuSet.stream().map(menu-> getNodeFunction(menu)).collect(Collectors.toList());
            return TreeUtil.build(allMenuTreeNode,parentId);
        }

        return null;
    }
}

package com.wm.auth.controller.sys;


import cn.hutool.core.lang.tree.Tree;
import com.wm.auth.service.ISysMenuService;
import com.wm.common.util.oauth2.UserUtils;
import com.wm.core.model.vo.base.BaseResp;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 角色和菜单关联表 前端控制器
 * </p>
 *
 * @author Wang Min
 * @since 2022-07-31
 */
@Controller
@RequestMapping("/sysRoleMenu")
public class SysRoleMenuController {

    @Resource
    private ISysMenuService iSysMenuService;

    //获取当前用户角色的菜单
    @GetMapping("/getCurrentUserMenu")
    @ResponseBody
    public BaseResp<List<Tree<Long>>> getCurrentUserMenu(Long parentId){
        List<String> roles = UserUtils.getRoles();
        List<Tree<Long>> treeList = iSysMenuService.getCurrentUserMenu(roles,parentId);
        return BaseResp.ok(treeList);
    }

    //返回角色的菜单
    @GetMapping("/getRoleMenu")
    @ResponseBody
    public BaseResp<List<Tree<Long>>> getRoleMenu(String roleCode,Long parentId){
        List<String> roles = new ArrayList<>();
        roles.add(roleCode);
        List<Tree<Long>> treeList = iSysMenuService.getCurrentUserMenu(roles,parentId);
        return BaseResp.ok(treeList);
    }

}


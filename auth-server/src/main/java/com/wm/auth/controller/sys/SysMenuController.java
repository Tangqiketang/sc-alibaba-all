package com.wm.auth.controller.sys;


import cn.hutool.core.lang.tree.Tree;
import com.wm.auth.service.ISysMenuService;
import com.wm.common.util.oauth2.UserUtils;
import com.wm.core.model.vo.base.BaseResp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 菜单管理 前端控制器
 * </p>
 *
 * @author Wang Min
 * @since 2022-07-31
 */
@RestController
@RequestMapping("/menu")
public class SysMenuController {

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

    //获取菜单树,和用户无关
    @GetMapping("/getMenuTree")
    @ResponseBody
    public BaseResp<List<Tree<Long>>> getMenuTree(boolean lazy,Long parentId){
        List<Tree<Long>> treeList = iSysMenuService.getMenuTree(lazy,parentId);
        return BaseResp.ok(treeList);
    }

}


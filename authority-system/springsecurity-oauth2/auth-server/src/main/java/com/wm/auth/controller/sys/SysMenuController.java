package com.wm.auth.controller.sys;


import cn.hutool.core.lang.tree.Tree;
import com.wm.auth.model.entity.SysMenu;
import com.wm.auth.service.ISysMenuService;
import com.wm.core.model.vo.base.BaseResp;
import org.springframework.web.bind.annotation.*;

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


    //获取菜单树,和用户无关
    @GetMapping("/getMenuTree")
    @ResponseBody
    public BaseResp<List<Tree<Long>>> getMenuTree(boolean lazy,Long parentId){
        List<Tree<Long>> treeList = iSysMenuService.getMenuTree(lazy,parentId);
        return BaseResp.ok(treeList);
    }


    //根据id查询菜单详情
    @GetMapping("/getMenuDetailById")
    @ResponseBody
    public BaseResp<SysMenu> getMenuDetailById(int id){
        SysMenu menu = iSysMenuService.getBaseMapper().selectById(id);
        return BaseResp.ok(menu);
    }

    @PostMapping("/addMenu")
    @ResponseBody
    public BaseResp addMenu(@RequestBody SysMenu sysMenu){
        return null;
    }


}


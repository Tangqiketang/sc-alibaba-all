package com.wm.sharding.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wm.core.model.vo.base.BaseResp;
import com.wm.sharding.mapper.IpcCameraMapper;
import com.wm.sharding.model.entity.IpcCamera;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Wang Min
 * @since 2022-06-01
 */
@Controller
@RequestMapping("/camera")
@Api(value = "相机",tags={"相机相关"})
@Slf4j
public class IpcCameraController {

    @Resource
    private IpcCameraMapper ipcCameraMapper;


    @ApiOperation(value = "新增相机", notes = "相机")
    @PostMapping("/insertCamera")
    @ResponseBody
    public BaseResp insertCamera(@Validated @RequestBody IpcCamera camera ){
        BaseResp rsp = new BaseResp();
        ipcCameraMapper.insert(camera);
        rsp.setCode("0");
        return rsp;
    }


    @GetMapping("/test")
    @ResponseBody
    public String test(){
        LambdaQueryWrapper<IpcCamera> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        return ipcCameraMapper.selectList(lambdaQueryWrapper).toString();
    }




}


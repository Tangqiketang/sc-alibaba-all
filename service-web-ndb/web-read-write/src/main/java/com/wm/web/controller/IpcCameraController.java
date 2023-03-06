package com.wm.web.controller;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wm.core.model.vo.base.BaseResp;
import com.wm.web.mapper.IpcCameraMapper;
import com.wm.web.model.entity.IpcCamera;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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


    @ApiOperation(value = "查询所有相机", notes = "查询所有相机")
    @GetMapping("/list")
    @ResponseBody
    //多数据源事务注解
    @DSTransactional
    //slave_1 slave_2 会自动负载读取
    @DS("slave")
    public BaseResp list(){
        BaseResp rsp = new BaseResp();
        List<IpcCamera> list = ipcCameraMapper.selectList(new LambdaQueryWrapper<IpcCamera>());
        rsp.setCode("0");
        rsp.setResult(list);
        return rsp;
    }


    @ApiOperation(value = "新增相机", notes = "相机")
    @PostMapping("/insertCamera")
    @ResponseBody
    public BaseResp insertCamera(@Validated @RequestBody IpcCamera camera ){
        BaseResp rsp = new BaseResp();
        ipcCameraMapper.insert(camera);

       // throw new RuntimeException();

        rsp.setCode("0");
        return rsp;
    }



}


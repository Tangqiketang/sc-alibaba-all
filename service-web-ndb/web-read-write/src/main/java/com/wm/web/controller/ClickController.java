package com.wm.web.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wm.core.model.vo.base.BaseResp;
import com.wm.web.mapper.DevicePropHistoryMapper;
import com.wm.web.model.entity.DevicePropHistory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Wang Min
 * @since 2022-06-01
 */
@Controller
@RequestMapping("/click")
@Api(value = "click",tags={"click相关"})
@Slf4j
public class ClickController {

    @Resource
    private DevicePropHistoryMapper devicePropHistoryMapper;


    @ApiOperation(value = "查询所有设备属性", notes = "查询所有设备属性")
    @GetMapping("/list")
    @ResponseBody
    public BaseResp list(){
        BaseResp rsp = new BaseResp();
        List<DevicePropHistory> list = devicePropHistoryMapper.selectList(new LambdaQueryWrapper<DevicePropHistory>());
        rsp.setCode("0");
        rsp.setResult(list);
        return rsp;
    }


    @ApiOperation(value = "新增设备属性", notes = "新增设备属性")
    @PostMapping("/insertDeviceProperty")
    @ResponseBody
    public BaseResp insertDeviceProperty(@Validated @RequestBody DevicePropHistory deviceProp){
        BaseResp rsp = new BaseResp();
        devicePropHistoryMapper.insert(deviceProp);
        rsp.setCode("0");
        return rsp;
    }



}


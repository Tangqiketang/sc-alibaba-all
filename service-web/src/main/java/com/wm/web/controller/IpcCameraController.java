package com.wm.web.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wm.core.model.vo.base.BaseResp;
import com.wm.redis.spi.TestSpiService;
import com.wm.web.mapper.IpcCameraMapper;
import com.wm.web.model.entity.IpcCamera;
import com.wm.web.service.IIpcCameraService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.ServiceLoader;

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


    //查询详情
    @ApiOperation(value = "查询xxx详情", notes = "查询xxx详情")
    @GetMapping("/getXXXDetail")
    @ResponseBody
    public BaseResp<Object> getXXXDetail(
            @ApiParam(value = "唯一编号") @RequestParam(required = true) String userKey,
            @ApiParam(value = "产品编号") @RequestParam(required = true) String productKey,
            @ApiParam(value = "设备名称") @RequestParam(required = true) String deviceName){
        BaseResp rsp = new BaseResp();
        Object obj = new Object();
        return BaseResp.ok(obj);
    }





    @GetMapping("/test")
    @ResponseBody
    public String test(){
        LambdaQueryWrapper<IpcCamera> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        return ipcCameraMapper.selectList(lambdaQueryWrapper).toString();
    }


    @ApiOperation(value = "分页按Long时间查询", notes = "分页按Long时间查询")
    @GetMapping("/getByLongTimePage")
    @ResponseBody
    public BaseResp<IPage<Object>> getByLongTimePage(
            @ApiParam(value = "产品编号") @RequestParam(required = true) String productKey,
            @ApiParam(value = "开始时间")@RequestParam(name="startTime",required = true)Long startTime,
            @ApiParam(value = "结束时间")@RequestParam(name="endTime",required = true)Long endTime,
            @Min(value = 1, message = "最小值为1") @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
            @Range(min = 1, max = 100, message = "pageSize必须在1到100之间")@RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        BaseResp<IPage<Object>> result = new BaseResp<>();
        IPage<Object> page = new Page<>(pageNo,pageSize);
        Page<Object> list = null;
        result.setResult(list);
        return result;
    }

    @ApiOperation(value = "分页按LocalDateTime时间查询", notes = "分页按LocalDateTime时间查询")
    @GetMapping("/getByLocalDateTimePage")
    @ResponseBody
    public BaseResp<IPage<Object>> getByLocalDateTimePage(
            @ApiParam(value = "产品编号") @RequestParam(required = true) String productKey,
            @ApiParam(value = "开始时间", required = true) @RequestParam(name = "startTime")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @ApiParam(value = "结束时间", required = true) @RequestParam(name = "endTime")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @Min(value = 1, message = "最小值为1") @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
            @Range(min = 1, max = 100, message = "pageSize必须在1到100之间")@RequestParam(name="pageSize", defaultValue="10") Integer pageSize){

        BaseResp<IPage<Object>> result = new BaseResp<>();
        IPage<Object> page = new Page<>(pageNo,pageSize);
        //WHERE create_time BETWEEN #{startTime} AND #{endTime}
        //queryWrapper.eq(UserDeviceDetector::getImei,imei).in(UserDeviceDetector::getSleepStatusChange,Arrays.asList(1,2));
        //queryWrapper.apply("date_format(create_time,'%Y-%m-%d')={0}",queryDate);
        Page<Object> list = null;
        result.setResult(list);
        return result;
    }

    @Resource
    private IIpcCameraService iIpcCameraService;

    @GetMapping("/test12")
    @ResponseBody
    public String test12(){
        return iIpcCameraService.asycDo();
    }


    @GetMapping("/testspi")
    @ResponseBody
    public String testspi(){
        ServiceLoader<TestSpiService> internetServices = ServiceLoader.load(TestSpiService.class);
        for (TestSpiService internetService : internetServices) {
            System.out.println(internetService.getServiceImplName());
        }
        return null;
    }

}


package com.wm.web.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wm.common.util.RestTemplateKit;
import com.wm.core.model.vo.base.BaseResp;
import com.wm.redis.constant.BusinessTypeEnum;
import com.wm.redis.spi.TestSpiService;
import com.wm.redis.util.RedisKit;
import com.wm.web.aop.annotation.NoRepeatSubmit;
import com.wm.web.groupValidate.IpcCameraInsertGroup;
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
import org.springframework.web.context.request.async.WebAsyncTask;

import javax.annotation.Resource;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

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
    public BaseResp insertCamera(@Validated({IpcCameraInsertGroup.class}) @RequestBody IpcCamera camera ){
        BaseResp rsp = new BaseResp();
        ipcCameraMapper.insert(camera);

       // throw new RuntimeException();

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


    @Resource
    private RestTemplateKit restTemplateKit;

    @GetMapping("/test")
    @ResponseBody
    @NoRepeatSubmit
    public String test(){
        Map<String,String> map = new HashMap<>();
        map.put("name","testwm");
        String result = restTemplateKit.get("http://localhost:8762/hi?name={name}",String.class,map);
        log.info("xxx:"+result);
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
        //select b.user_id,count(1)  from user_use_log b where b.visite_time <= '2022-6-05' and b.behavior_type =1 group by user_id  having count(*)>500 and count(*)<1000
        //
        //select b.id,b.user_id  from user_use_log b where b.visite_time <= '2022-6-05' and b.behavior_type =1 order by user_id ;
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

    @Resource
    private RedisKit redisKit;

    @GetMapping("/testredis")
    @ResponseBody
    public String testredis(){
        List<IpcCamera> list = ipcCameraMapper.selectList(new LambdaQueryWrapper<IpcCamera>());
        Map<String,Object> map= list.stream().collect(Collectors.toMap(IpcCamera::getCameraName, a -> (Object)a,(k1, k2)->k1));
        String key = "cameraKey";
/*        redisKit.opsForHash().putAll(key,map);

        Map<String,Object> fieldValue = redisKit.opsForHash().entries(key);
        IpcCamera result = (IpcCamera) fieldValue.get("name1");

       redisKit.set("aaa",list.get(0));
        String ccc = (String) redisKit.get("aaa");
        IpcCamera bbb = (IpcCamera) redisKit.get("aaa");*/

/*        redisKit.setBit("a0",0L,true);
        redisKit.setBit("a1",1L,true);
        redisKit.setBit("a2",2L,true);
        redisKit.setBit("a3",3L,true);
        redisKit.setBit("a4",4L,true);
        redisKit.setBit("a5",5L,true);
        redisKit.setBit("a6",6L,true);
        redisKit.setBit("a7",7L,true);
        redisKit.setBit("a8",8L,true);
        redisKit.setBit("a9",9L,true);
        redisKit.setBit("a10",10L,true);

        List<Long> result = redisKit.getBitList("a10");
        System.out.println(JSON.toJSONString(result));*/


        return redisKit.generate(BusinessTypeEnum.ORDER,8);

        //return JSON.toJSONString(null);
    }

    /*============     ===================================================================================**/

    @GetMapping(path = "get")
    public WebAsyncTask<String> get(long sleep, boolean error) {

        Callable<String> callable = () -> {
            System.out.println("do some thing");
            Thread.sleep(sleep);
            if (error) {
                System.out.println("出现异常，返回!!!");
                throw new RuntimeException("异常了!!!");
            }
            return "hello world";
        };
        // 指定3s的超时
        WebAsyncTask<String> webTask = new WebAsyncTask<>(3000, callable);
        webTask.onCompletion(() -> System.out.println("over!!!"));

        webTask.onTimeout(() -> {
            System.out.println("超时了");
            return "超时返回!!!";
        });

        webTask.onError(() -> {
            System.out.println("出现异常了!!!");
            return "异常返回";
        });
        return webTask;
    }




}


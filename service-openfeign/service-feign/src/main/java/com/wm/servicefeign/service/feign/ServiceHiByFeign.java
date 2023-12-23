package com.wm.servicefeign.service.feign;

import lombok.Data;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

//@FeignClient(value = "service-hi",fallback = ServiceHiByFeignFallback.class,configuration = HiFeignClientConfig.class)
//@FeignClient(value = "service-hi",fallback = ServiceHiByFeignFallback.class)
@FeignClient(name = "service-hi")
public interface ServiceHiByFeign {
    //普通表单
    @RequestMapping(value = "/hi",method = RequestMethod.GET)
    String getHiFromServiceHi(@RequestParam(value = "name") String name);

    //json
    @PostMapping(value = "/hibody")
    String homebody(@RequestBody Student student);

    //pojo表单
    @PostMapping(value = "/hibody2")
    String homebody2(@SpringQueryMap Student student);

    //url带参数
    @GetMapping(value = "/hibody3/{id}")
    String homebody3(@PathVariable("id")Integer id);


    @Data
    public class Student{
        private String name;
        private String sex;
    }

}

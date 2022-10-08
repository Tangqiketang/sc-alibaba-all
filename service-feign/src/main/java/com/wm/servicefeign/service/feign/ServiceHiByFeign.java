package com.wm.servicefeign.service.feign;

import com.wm.servicefeign.service.feign.fallback.ServiceHiByFeignFallback;
import lombok.Data;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "service-hi",fallback = ServiceHiByFeignFallback.class)
public interface ServiceHiByFeign {

    @RequestMapping(value = "/hi",method = RequestMethod.GET)
    String getHiFromServiceHi(@RequestParam(value = "name") String name);


    @RequestMapping(value = "/hibody",method = RequestMethod.GET)
    String homebody(@RequestBody Student student);


    @Data
    public class Student{
        private String name;
        private String sex;
    }

}

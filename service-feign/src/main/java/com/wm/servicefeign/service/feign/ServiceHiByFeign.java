package com.wm.servicefeign.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "service-hi")
public interface ServiceHiByFeign {

    @RequestMapping(value = "/hi",method = RequestMethod.GET)
    String getHiFromServiceHi(@RequestParam(value = "name") String name);

}

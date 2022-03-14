package com.wm.servicefeign.service.feign;

import com.wm.servicefeign.service.feign.fallback.ServiceHiByFeignFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "service-hi",fallback = ServiceHiByFeignFallback.class)
public interface ServiceHiByFeign {

    @RequestMapping(value = "/hi",method = RequestMethod.GET)
    String getHiFromServiceHi(@RequestParam(value = "name") String name);

}

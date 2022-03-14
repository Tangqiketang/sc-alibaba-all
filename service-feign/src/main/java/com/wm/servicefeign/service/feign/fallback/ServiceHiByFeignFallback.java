package com.wm.servicefeign.service.feign.fallback;

import com.wm.servicefeign.service.feign.ServiceHiByFeign;
import org.springframework.stereotype.Component;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-02-13 0:28
 */
@Component
public class ServiceHiByFeignFallback implements ServiceHiByFeign {


    @Override
    public String getHiFromServiceHi(String name) {
        return "sorry, you are fail,"+name;
    }
}
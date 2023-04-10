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

    @Override
    public String homebody(Student student) {
        return "faile body xx";
    }

    @Override
    public String homebody2(Student student) { return "faile body2 xx"; }

    @Override
    public String homebody3(Integer id) {
        return "id:"+id;
    }
}
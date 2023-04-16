package com.wm.dubbocomsumer;

import com.wm.core.model.exception.meta.ServiceException;
import com.wm.dubbocomsumer.service.DubboService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-05-31 17:51
 */
@SpringBootApplication
@EnableDubbo
@RestController
@Slf4j
public class DubboConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DubboConsumerApplication.class,args);
    }

    @Resource
    private DubboService dubboService;

    @RequestMapping("/test")
    public String home() {
        try{
            dubboService.test();
        }catch (ServiceException e){
            log.info("抓到异常了");
        }
        return null;
    }

}
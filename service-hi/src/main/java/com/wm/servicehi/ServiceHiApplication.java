package com.wm.servicehi;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.*;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-02-22 16:28
 */
@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class ServiceHiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceHiApplication.class,args);
    }

    @Value("${server.port}")
    String port;

    //普通表单
    @RequestMapping("/hi")
    public String home(@RequestParam(value = "name", defaultValue = "wm") String name) {
        try{
            Thread.sleep(500);
        }catch (Exception e){}

        return "hi " + name + " ,i am from port:" + port;
    }
    //json
    @PostMapping("/hibody")
    public String homebody(@RequestBody Student student) {
        return JSON.toJSONString(student);
    }
    //pojo
    @PostMapping("/hibody2")
    public String homebody2(Student student) {
        return JSON.toJSONString(student);
    }
    //url带参数
    @GetMapping("/hibody3/{id}")
    public String homebody3(@PathVariable("id ") Integer id) {
        return "hi id:"+id;
    }

}

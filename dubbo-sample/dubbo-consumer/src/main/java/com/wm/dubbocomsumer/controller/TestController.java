package com.wm.dubbocomsumer.controller;

import com.wm.dubbocomsumer.service.DubboService;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-07-01 9:22
 */
@RestController
public class TestController {

    @Resource
    private DubboService dubboService;

}
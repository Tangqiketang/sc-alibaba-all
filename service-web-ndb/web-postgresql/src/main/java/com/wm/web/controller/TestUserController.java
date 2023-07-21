package com.wm.web.controller;


import com.wm.core.model.vo.base.BaseResp;
import com.wm.web.mapper.TestUserMapper;
import com.wm.web.model.entity.TestUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/user")
@Api(value = "用户",tags={"用户"})
@Slf4j
public class TestUserController {
    @Resource
    private TestUserMapper testUserMapper;

    @ApiOperation(value = "查询所有用户value", notes = "查询所有用户notes")
    @GetMapping("/list")
    @ResponseBody
    public BaseResp list(){
        BaseResp rsp = new BaseResp();
        List<TestUser> list = testUserMapper.selectList(null);
        rsp.setCode("0");
        rsp.setResult(list);
        return rsp;
    }

    @ApiOperation(value = "新增用户value", notes = "新增用户notes")
    @GetMapping("/get")
    @ResponseBody
    public BaseResp insert(){
        BaseResp rsp = new BaseResp();
        TestUser po = new TestUser().setName("wm").setAge(11).setCreateTime(LocalDate.now());
        TestUser.Family family = new TestUser.Family().setNick("NICK昵称");
/*        List<TestUser.Family> list = new ArrayList<>();
        list.add(family);*/
        po.setFamily(family);
        testUserMapper.insert(po);
        return rsp;
    }

}


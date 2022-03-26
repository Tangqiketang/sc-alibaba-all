package com.wm.rocket;

import com.wm.rocket.sender.RocketSendService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2021-03-02 10:15
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseTest {

   @Resource
    private RocketSendService rocketSendService;

   @Test
   public void test01(){
       rocketSendService.sendWM1();
   }

    @Test
    public void test02(){
        rocketSendService.sendWM2();
    }

    @Test
    public void testDelay(){
        rocketSendService.sendDelay();
    }

    @Test
    public void testTag(){
        rocketSendService.sendTag();
    }

}


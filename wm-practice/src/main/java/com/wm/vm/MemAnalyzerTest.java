package com.wm.vm;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2020-03-27 16:35
 */
public class MemAnalyzerTest {



    public static void main(String[] args) {
        testVMOver();
    }

    public static  void testVMOver(){
        System.out.println("开始");
        List<MemUser> wmList = new ArrayList<>();
        for(int i=0;i<100000;i++){
            MemUser user = new MemUser();
            user.setName("wangmin-"+i);
            wmList.add(user);
        }

        System.out.println("结束"+wmList.size());
    }

}
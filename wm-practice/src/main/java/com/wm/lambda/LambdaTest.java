package com.wm.lambda;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-12-19 10:25
 */
public class LambdaTest {

    private static List<Device> list = new ArrayList<>();
    private static Map<String,Device> map = new HashMap();


    /* ====================================List============================================= */
    //遍历
    public void listFor(){
        list.forEach(a->{
        });
    }
    //根据某一个属性分组
    public void groupByImei(){
        Map<String,Device> map1 =       list.stream().collect(Collectors.toMap(Device::getImei, Function.identity()));
        Map<String,Device> map2 =       list.stream().collect(Collectors.toMap(Device::getImei,a->a,(k1,k2)->k1));
        Map<String,List<Device>> map3 = list.stream().collect(Collectors.groupingBy(Device::getImei));
    }




    //排序 List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
    //        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
    //            // 降序排序
    //            @Override
    //            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
    //                return o2.getValue().compareTo(o1.getValue());
    //            }
    //
    //        });


    /* =====================================Map============================================ */
    //遍历
    public void mapFor(){
        map.forEach((k,v)->{
        });
    }

}

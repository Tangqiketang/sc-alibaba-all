package com.wm.lambda;

import cn.hutool.core.collection.CollUtil;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-12-19 10:25
 */
public class LambdaTest {

    private static List<Device> list = new ArrayList<>();
    private static Map<String,Device> map = new HashMap();

    private static Map<String,List<Device>> mapList = new HashMap<>();

    static {
        Device d1 = new Device("imei11","proKey11","devName11",1d);
        Device d2 = new Device("imei22","proKey22","devName22",2d);

        Device d3 = new Device("imei33","proKey11","devName33",1d);
        list.add(d1);list.add(d2);list.add(d3);

        mapList.put("key1",list);

    }


    /* ====================================List============================================= */
    /* | stream of elements +-----> |filter+-> |sorted+-> |map+-> |collect| */
    //遍历
    public void listFor(){
        list.forEach(a->{
        });
    }

    //根据某一个属性分组
    public static void groupByImei(){
        //Map<String,Device> map1 =       list.stream().map(a->a).collect(Collectors.toMap(Device::getProductKey, Function.identity()));
        Map<String,Device> map2 =       list.stream().map(a->a).collect(Collectors.toMap(Device::getProductKey,a->a,(k1,k2)->k1));
        Map<String,List<Device>> map3 = list.stream().map(a->a).collect(Collectors.groupingBy(Device::getProductKey));

        //排序
        List<Device> sortedList =       list.stream().sorted((d1,d2)->d1.getPrice().compareTo(d2.getPrice()))
                                                                                .collect(Collectors.toList());
        Collections.sort(list,(d1,d2)->d1.getPrice().compareTo(d2.getPrice()));

        //Device dev = Stream.of(list).filter().findAny().;
        //System.out.println("dev:"+dev);
    }

    //对某个属性进行操作
    public static void aggre(){
        //根据id去重
        List<Device>       list1=       list.stream().map(a->a).filter(a->a!=null).collect(Collectors.toList())
                //继续去重
                .stream().collect(Collectors.collectingAndThen(Collectors.toCollection(()->new TreeSet<>(Comparator.comparing(Device::getProductKey))),ArrayList::new));
        //把某一个属性用逗号拼接
        String imeiString = list.stream().map(Device::getImei).collect(Collectors.joining(","));

        //查找最大值
        Optional<Device> maxPriceDev = list.stream().collect(Collectors.maxBy(Comparator.comparing(Device::getPrice)));

        //求和/average/max/count/
        double     sumPrice =  list.stream().mapToDouble(Device::getPrice).sum();
        BigDecimal sumPrice2 = list.stream().map(a-> BigDecimal.valueOf(a.getPrice())).reduce(BigDecimal.ZERO,BigDecimal::add);

        //获取每个设备的名字的单词去重 flatMap->从n个集合中的元素展平到一个流
        List<String> deviceNameWordDistinctList = list.stream()
                .map(device->device.getDeviceName().split(""))
                .flatMap(name->Arrays.stream(name))
                .distinct().collect(Collectors.toList());
        System.out.println(Arrays.deepToString(deviceNameWordDistinctList.toArray())); //[d, e, v, N, a, m, 1, 2, 3] */

        //list.stream().flatMap(device->Stream.of(device)).distinct().collect(Collectors.toList());


        //字符串拆分并去重
        ArrayList<String> strList = CollUtil.newArrayList("ADEF", "ABC", "GHI");
        List<String> collect = strList.stream().flatMap(ele -> Stream.of(ele.split(""))).distinct().collect(Collectors.toList());
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
        //Map<String,Device>
        map.forEach((k,v)->{
        });

        //Map<String,List<Device>>  展平成--> List<Device>
        mapList.entrySet().stream().flatMap(entry->entry.getValue().stream()).collect(Collectors.toList());
    }



    public static void main(String[] args) {
        //groupByImei();
        aggre();
    }
}

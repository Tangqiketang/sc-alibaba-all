package com.wm.reactor.flux;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-06-08 10:09
 */
public class FluxTest {

    // just()：创建Flux序列，并声明指定数据流

    // subscribe()：订阅Flux序列，只有进行订阅后才回触发数据流，不订阅就什么都不会发生


    public static void main(String[] args) {
        ArrayList<Integer> list = Lists.newArrayList(1, 2, 3, 4);
        Flux<Integer> flux = Flux.fromIterable(list);
        list.add(5);
        flux.subscribe(System.out::println);

    }

    @Data
    @AllArgsConstructor
    class Student {
        private String name;
        private Integer age;
    }

}
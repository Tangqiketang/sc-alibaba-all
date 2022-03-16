package com.wm.servicemqtt.flux;

import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-03-14 15:56
 */
public class TestDemo {

    public static void main(String[] args) {

        System.out.println(allUsers());
        System.out.println(allUsers().count());
        System.out.println(allUsers().collect(Collectors.toList()));
        System.out.println(allUsers().findAny().get());

        List<String> originList = Arrays.asList("abc", "", "bc", "efg", "abcd","", "jkl");
        String xx = originList.stream().filter(a->!a.isEmpty()).collect(Collectors.joining(""));
        System.out.println(xx);
    }


    public static Stream<UserClient> allUsers(){
        return Stream.of(new UserClient("AAA","BBB"),new UserClient("ccc","ddd"));
    }


}

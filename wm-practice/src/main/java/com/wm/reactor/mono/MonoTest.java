package com.wm.reactor.mono;

import reactor.core.publisher.Mono;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2023-04-28 23:36
 */
public class MonoTest {

    public static void main(String[] args) {
        printString("wxc");
    }

    public static void printString(String s){
        Mono.just(s).defaultIfEmpty("wm").map(a -> a+"zyy").subscribe(System.out::printf);
    }

}
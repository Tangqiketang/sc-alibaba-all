package com.wm.thread.completableFuture;

import com.alibaba.fastjson.JSON;
import com.wm.thread.completableFuture.vo.MemberAddressDTO;
import com.wm.thread.completableFuture.vo.OrderConfirmDTO;
import com.wm.thread.completableFuture.vo.OrderConfirmVO;
import com.wm.thread.completableFuture.vo.OrderItemDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 描述:异步编排
 *
 * @auther WangMin
 * @create 2022-09-06 10:16
 */
public class ComplatableFutureTest {

    ThreadPoolExecutor executor = new ThreadPoolExecutor(2,4,1000L, TimeUnit.MILLISECONDS,new LinkedBlockingDeque<>());

    public  OrderConfirmVO confirm(OrderConfirmDTO orderConfirmDTO) {
        OrderConfirmVO resultVo = new OrderConfirmVO();

        //不需要返回结果的方式
        CompletableFuture<Void> voidFuture = CompletableFuture.runAsync(()->{
            //订单明细
            List<OrderItemDTO> orderItems = new ArrayList<>();
            OrderItemDTO dto = new OrderItemDTO();
            dto.setSkuName("test111name");
            orderItems.add(dto);
            try{
                Thread.sleep(5000);
            }catch (Exception e){}
            resultVo.setOrderItems(orderItems);
        },executor);

        CompletableFuture<List<MemberAddressDTO>> addressFuture = CompletableFuture.supplyAsync(() -> {
            List<MemberAddressDTO> addressDTOS = new ArrayList<>();
            MemberAddressDTO dto = new MemberAddressDTO();
            dto.setCity("test111city");
            addressDTOS.add(dto);
            try{
                Thread.sleep(5000);
            }catch (Exception e){}
            return addressDTOS;
        }, executor).thenApplyAsync(c->{return c;});

        //等待两个future执行完成
        CompletableFuture.allOf(voidFuture,addressFuture).join();

        return resultVo;
    }

    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis()/1000);
        ComplatableFutureTest test = new ComplatableFutureTest();
        OrderConfirmVO confirm = test.confirm(new OrderConfirmDTO());
        System.out.println(JSON.toJSONString(confirm));
        System.out.println(System.currentTimeMillis()/1000);
    }


}



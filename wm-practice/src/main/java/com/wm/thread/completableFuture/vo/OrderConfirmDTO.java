package com.wm.thread.completableFuture.vo;

import lombok.Data;

/**
 * @desc 订单提交实体类
 */
@Data
public class OrderConfirmDTO {

    //商品id
    private Long skuId;

    private Integer count;

}

package com.wm.lambda;

import lombok.Data;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-12-19 10:22
 */
@Data
public class Device {

    private String imei;

    private String productKey;

    private String deviceName;

    private Double price;
}

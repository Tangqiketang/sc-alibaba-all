package com.wm.lambda;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-12-19 10:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Device {

    private String imei;

    private String productKey;

    private String deviceName;

    private Double price;
}

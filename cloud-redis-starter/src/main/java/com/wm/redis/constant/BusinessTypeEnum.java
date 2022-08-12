package com.wm.redis.constant;

import lombok.Getter;

public enum BusinessTypeEnum {

    ORDER(300, "订单");

    @Getter
    private Integer value;

    @Getter
    private String label;

    BusinessTypeEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }

}

package com.cetiti.iot.mqtt.constant;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-04-11 11:17
 */
public enum ProductFuncTypeEnum {
    PROP("PROP", "属性"),
    EVENT("EVENT", "事件"),
    SERVICE("SERVICE", "服务");
    String type;
    String msg;

    ProductFuncTypeEnum(String type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public static ProductFuncTypeEnum explain(String type){
        for(ProductFuncTypeEnum item:ProductFuncTypeEnum.values()){
            if(item.type.equals(type)){
                return item;
            }
        }
        return ProductFuncTypeEnum.PROP;
    }

}

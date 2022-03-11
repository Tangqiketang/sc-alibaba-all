package com.wm.servicemqtt.mqtt.topic;

/**
 * 订阅的topic
 */
public enum TopicName {

    DEFAULT_CONSUME_TOPIC(1,"from/device/topic/patrol/#","test1"),
    DEFAULT_PRODUCER_TOPIC(2,"send/device/topic/","哈哈"),
    DEFAULT_WILL_TOPIC(3,"send/device/connect/lost","xxx"),
    DEVICE_ONLINE(4,"/device/online/#","xxx"),
    DEVICE_aaa(5,"/t/#","xxx"),
    DEVICE_OFFLINE(6,"/device/offline/#","xxx");


    private final Integer key;
    private final String value;
    private final String desc;

    private TopicName(Integer key,String value,String desc){
        this.key = key;
        this.value = value;
        this.desc = desc;
    }
    public Integer getKey() {
        return key;
    }
    public String getValue() {
        return value;
    }
    public String getDesc(){return desc;}

    @Override
    public String toString() {
        return this.value;
    }
}

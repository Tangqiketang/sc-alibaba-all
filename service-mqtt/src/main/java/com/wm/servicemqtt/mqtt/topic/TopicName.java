package com.wm.servicemqtt.mqtt.topic;

public enum TopicName {

    DEFAULT_CONSUME_TOPIC(1,"from/device/topic/patrol/#"),
    DEFAULT_PRODUCER_TOPIC(2,"send/device/topic/"),
    DEFAULT_WILL_TOPIC(3,"send/device/connect/lost"),
    DEVICE_ONLINE(4,"/device/online/#"),
    DEVICE_OFFLINE(5,"/device/offline/#");


    private final Integer key;
    private final String value;

    private TopicName(Integer key,String value){
        this.key = key;
        this.value = value;
    }
    public Integer getKey() {
        return key;
    }
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}

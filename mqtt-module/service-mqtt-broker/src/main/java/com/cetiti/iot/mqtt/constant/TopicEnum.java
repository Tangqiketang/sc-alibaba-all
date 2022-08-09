package com.cetiti.iot.mqtt.constant;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-03-30 15:39
 */
public enum TopicEnum {
    //属性上传
    PROPERTY_POST(      "/sys/${productKey}/${deviceName}/thing/event/property/post" ,"/sys/+/+/thing/event/property/post","设备属性上报"),
    PROPERTY_REPLY(     "/sys/${productKey}/${deviceName}/thing/event/property/post_reply","/sys/+/+/thing/event/property/post_reply","云端响应属性上报"),
    //属性设置
    PROPERTY_SET(       "/sys/${productKey}/${deviceName}/thing/service/property/set","/sys/+/+/thing/service/property/set","下发设备属性设置"),
    PROPERTY_SET_REPLY( "/sys/${productKey}/${deviceName}/thing/service/property/set_reply","/sys/+/+/thing/service/property/set_reply","设备响应属性设备"),
    //事件上报
    EVENT_POST(         "/sys/${productKey}/${deviceName}/thing/event/${tsl.event.identifier}/post","/sys/+/+/thing/event/+/post","设备事件上报"),
    EVENT_POST_REPLY(   "/sys/${productKey}/${deviceName}/thing/event/${tsl.event.identifier}/post_reply","/sys/+/+/thing/event/+/post_reply","云端响应事件上报"),
    //服务调用设备
    SERVICE(            "/sys/${productKey}/${deviceName}/thing/service/${tsl.service.identifier}","/sys/+/+/thing/service/+","设备服务调用"),
    SERVICE_REPLY(      "/sys/${productKey}/${deviceName}/thing/service/${tsl.service.identifier}_reply","/sys/+/+/thing/service/+","设备端响应服务调用"),
    //子设备注册
    SUB_REGISTER(       "/sys/${productKey}/${deviceName}/thing/sub/register","/sys/+/+/thing/sub/register","子设备的MQTT动态注册请求"),
    SUB_REGISTER_REPLY( "/sys/${productKey}/${deviceName}/thing/sub/register_reply","/sys/+/+/thing/sub/register_reply","对子设备的MQTT动态注册响应"),
    //网关类型添加子设备
    TOPO_ADD(           "/sys/${productKey}/${deviceName}/thing/topo/add","/sys/+/+/thing/topo/add","网关类型设备添加设备拓扑关系"),
    TOPO_ADD_REPLY(     "/sys/${productKey}/${deviceName}/thing/topo/add_reply","/sys/+/+/thing/topo/add_reply","对网关类型设备添加设备拓扑关系的响应"),
    //子设备上线
    SUB_LOGIN(          "/ext/session/${productKey}/${deviceName}/combine/login","/ext/session/+/+/combine/login","子设备上线"),
    SUB_LOGIN_REPLY(    "/ext/session/${productKey}/${deviceName}/combine/login_reply","/ext/session/+/+/combine/login_reply","子设备上线的回复"),
    //
    PRO_DESIRED_GET(    "/sys/${productKey}/${deviceName}/thing/property/desired/get","/sys/+/+/thing/property/desired/get","设备向云端请求获取设备属性的期望值"),
    PRO_DESIRED_REPLY(  "/sys/${productKey}/${deviceName}/thing/property/desired/get_reply","/sys/+/+/thing/property/desired/get_reply","获取设备属性的期望值响应"),
    //
    THING_DISABLE(      "/sys/${productKey}/${deviceName}/thing/disable","/sys/+/+/thing/disable","下发禁用子设备"),
    THING_DISABLE_REPLY("/sys/${productKey}/${deviceName}/thing/disable_reply","/sys/+/+/thing/disable_reply","设备禁用子设备响应"),
    THING_ENABLE(       "/sys/${productKey}/${deviceName}/thing/enable","/sys/+/+/thing/enable","下发启用被禁用的子设备"),
    THING_ENABLE_REPLY( "/sys/${productKey}/${deviceName}/thing/enable_reply","/sys/+/+/thing/enable_reply","启用被禁用的子设备设备响应"),
    THING_DELETE(       "/sys/${productKey}/${deviceName}/thing/delete","/sys/+/+/thing/delete","下发删除子设备"),
    THING_DELETE_REPLY( "/sys/${productKey}/${deviceName}/thing/delete_reply","/sys/+/+/thing/delete_reply","删除子设备响应"),
    //OTA升级
    OTA_INFORM(         "/ota/device/inform/${productKey}/${deviceName}","/ota/device/inform/+/+","设备上报OTA模块版本"),
    OTA_UPGRADE(        "/ota/device/upgrade/${productKey}/${deviceName}","/ota/device/upgrade/+/+","下发推送OTA升级包信息"),
    OTA_PROGRESS(       "/ota/device/progress/${productKey}/${deviceName}","/ota/device/progress/+/+","设备上报升级进度"),
    SYS_OTA_GET(        "/sys/${productKey}/${deviceName}/thing/ota/firmware/get","/sys/+/+/thing/ota/firmware/get","设备上报升级包信息"),
    SYS_OTA_REPLY(      "/sys/${productKey}/${deviceName}/thing/ota/firmware/get_reply","/sys/+/+/thing/ota/firmware/get_reply","平台下发升级包信息"),
    FILE_DOWNLOAD(      "/sys/${productKey}/${deviceName}/thing/file/download","/sys/+/+/thing/file/download","设备请求下载文件分片"),
    FILE_DOWNLOAD_REPLY("/sys/${productKey}/${deviceName}/thing/file/download_reply","/sys/+/+/thing/file/download_reply","下发文件分片");




    private final String topic;
    private final String topicWild;
    private final String desc;

    private TopicEnum(String topic, String topicWild, String desc){
        this.topic = topic;
        this.topicWild=topicWild;
        this.desc = desc;
    }




}

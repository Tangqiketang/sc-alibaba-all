package com.cetiti.iot.mqtt.bo.msg;

import com.cetiti.iot.mqtt.constant.ProductFuncTypeEnum;
import com.cetiti.iot.mqtt.constant.TopicEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-04-11 11:15
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceUpMessageBo {

    String        productKey;
    String deviceCode;
    String brokerId;

    //物模型类型枚举
    ProductFuncTypeEnum funcType;
    //原始上传的topic
    String topic;
    //topic对应的枚举
    TopicEnum topicEnum;

    String clientId;

    /**mqtt消息中的packetId*/
    Long packetId;
    //IBaseProtocol baseProtocolService;
    byte[]    sourceMsg;


    Date currTime = new Date();

}

package com.wm.servicemqtt.mqtt.conf;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-04-01 14:15
 */
@Component
@ConfigurationProperties(prefix = "mqtt")
@Data
@Accessors(chain = true)
public class MqttProperties {

   private String url;
   private String username;
   private String password;
   private String clientId;



}

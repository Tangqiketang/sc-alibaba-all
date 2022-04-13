package com.cetiti.iot.mqtt.handler.standHandler;

import com.cetiti.iot.mqtt.bo.ContextBo;
import com.cetiti.iot.mqtt.bo.WillMsgBo;
import com.cetiti.iot.mqtt.manager.SenderManager;
import com.cetiti.iot.mqtt.manager.SessionManager;
import com.cetiti.iot.mqtt.manager.WillMsgManager;
import com.cetiti.iot.mqtt.util.DecodeUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

/**
 * 描述:MQTT连接
 *
 * @auther WangMin
 * @create 2022-04-06 14:40
 */
@Component
@Slf4j
public class ConnectHandler {


    public void onConnect(ContextBo contextBo, MqttConnectMessage msg) {
        log.info("onConnect-handler请求:{}", msg);
        ChannelHandlerContext ctx = contextBo.getHandlerContext();

        MqttVersion version = MqttVersion.fromProtocolNameAndLevel(msg.variableHeader().name(), (byte) msg.variableHeader().version());
        String clientId = msg.payload().clientIdentifier();  //deviceName&productKey|securemode=2,signmethod,ext,timestamp|
        String userName = msg.payload().userName(); //deviceName&productKey
        String password = msg.payload().password();
        boolean cleanSession = msg.variableHeader().isCleanSession();
        contextBo.setVersion(version);
        contextBo.setClientId(clientId);
        contextBo.setUserName(userName);
        contextBo.setCleanSession(cleanSession);

        if (msg.variableHeader().keepAliveTimeSeconds() > 0 && msg.variableHeader().keepAliveTimeSeconds() <= contextBo.getKeepAliveMax()) {
            int keepAlive = msg.variableHeader().keepAliveTimeSeconds();
            contextBo.setKeepAlive(keepAlive);
        }
        
        if(!authCheck(contextBo, msg,password)){
            log.warn("用户名密码错误:{}", msg);
            contextBo.getHandlerContext().close();
            return;
        }

        SessionManager.removeContextByClientId(contextBo.getClientId());
        SessionManager.addConnect(contextBo.getClientId(), contextBo);
        contextBo.setConnected(true);
        //增加一个遗嘱信息
        addWillMsg(msg);
        //返回成功
        SenderManager.responseMsg(
                contextBo,
                MqttMessageFactory.newMessage(
                        new MqttFixedHeader(MqttMessageType.CONNACK, false, MqttQoS.AT_MOST_ONCE, false, 0x02),
                        new MqttConnAckVariableHeader(MqttConnectReturnCode.CONNECTION_ACCEPTED, !contextBo.getCleanSession()),
                        null),
                null,
                true);


    }

    private boolean authCheck(ContextBo contextBo, MqttConnectMessage msg,String password) {
        if(true){
            //暂时不开启权限校验 TODO
            return true;
        }else{
            // 三元组认证
            StringBuffer content = new StringBuffer();
            content.append("clientId").append(contextBo.getUserName())
                    .append("deviceName").append(contextBo.getUserName().split("&")[0])
                    .append("productKey").append(contextBo.getUserName().split("&")[1])
                    .append("timestamp").append(DecodeUtil.getAttributes(contextBo.getClientId().split("\\|")[1]).get("timestamp"));

            //TODO 后面从数据库获取设备密钥
            String deviceSecret = "326574b3d028f790aa1d0a5e9c48134d";

            if(DecodeUtil.sign(content.toString(),deviceSecret).toLowerCase(Locale.ROOT)
                    .equals(password.toLowerCase(Locale.ROOT))){
                return true;
            }else{
                return false;
            }
        }
    }

    private void addWillMsg(MqttConnectMessage msg){
        if (!msg.variableHeader().isWillFlag()) {
            return;
        }
        log.info("connecthandler.processWillMsg+++");
        MqttPublishMessage willMessage = (MqttPublishMessage) MqttMessageFactory.newMessage(
                new MqttFixedHeader(MqttMessageType.PUBLISH, false,
                        MqttQoS.valueOf(msg.variableHeader().willQos()),
                        msg.variableHeader().isWillRetain(), 0),
                new MqttPublishVariableHeader(msg.payload().willTopic(), 0),
                Unpooled.buffer().writeBytes(msg.payload().willMessageInBytes()));

        WillMsgBo willMsgBo = new WillMsgBo(msg.payload().clientIdentifier(),
                msg.payload().willTopic(), msg.variableHeader().isCleanSession(), willMessage);
        WillMsgManager.addWill(willMsgBo);
    }


    /**
     * hmac+签名算法 加密
     * @param content  内容
     * @param charset  字符编码
     * @param key	         加密秘钥
     * @param hamaAlgorithm hamc签名算法名称:例如HmacMD5,HmacSHA1,HmacSHA256
     * @return
     */
    public static String getHmacSign(String content, String charset,String key,String hamaAlgorithm){
        byte[] result = null;
        try {
            //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
            SecretKeySpec signinKey = new SecretKeySpec(key.getBytes(), hamaAlgorithm);
            //生成一个指定 Mac 算法 的 Mac 对象
            Mac mac = Mac.getInstance(hamaAlgorithm);
            //用给定密钥初始化 Mac 对象
            mac.init(signinKey);
            //完成 Mac 操作
            byte[] rawHmac;
            rawHmac = mac.doFinal(content.getBytes(charset));
            result = Base64.encodeBase64(rawHmac);

        } catch (NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
        } catch (InvalidKeyException e) {
            System.err.println(e.getMessage());
        }  catch (IllegalStateException | UnsupportedEncodingException e) {
            System.err.println(e.getMessage());
        }
        if (null != result) {
            return new String(result);
        } else {
            return null;
        }
    }




    public static void main(String[] args) {
        //阿里
        String hmacSign = getHmacSign("clientId12345deviceNamedeviceproductKeypktimestamp789", "UTF-8", "secret", "HmacSHA1");
        //
        String hmacSignXX = DecodeUtil.sign("clientIdtest&a15lHjvsGaUdeviceNametestproductKeya15lHjvsGaUtimestamp1649835394513",  "326574b3d028f790aa1d0a5e9c48134d");

        System.out.println(hmacSign);
        System.out.println(hmacSignXX);

    }
}

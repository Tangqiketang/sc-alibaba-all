package com.cetiti.iot.mqtt.util;

import com.alibaba.fastjson.JSON;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-04-13 15:47
 */
public class DecodeUtil {

    /**
     *
     * @param s 需加密的字符串
     * @param key 秘钥
     * @return 加密
     * @throws Exception
     */
    public static String sign(String s, String key) {
        //HMAC-SHA1 算法签名
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes("UTF-8"),
                    mac.getAlgorithm());
            mac.init(secretKeySpec);
            byte[] hash = mac.doFinal(s.getBytes("UTF-8"));
            String result = "";
            String tmp = "";
            for (byte bt : hash) {
                tmp = (Integer.toHexString(bt & 0xFF));
                if (tmp.length() == 1) {
                    result += "0";
                }
                result += tmp;
            }
            return result;
        }catch (Exception e){
            return "";
        }
    }



/*    public static Map<String,String> getKeyValue(String clientId){
        Arrays.stream(clientId.split("\\|")[1].split(",")).map(s->s.split("=")).collect(Collectors.toMap())
    }*/

    public static Map<String, String> getAttributes(String attributes) {
        Map<String, String> attr = new HashMap<>();
        Matcher m = Pattern.compile("(\\w+)=(.*?)(?=,\\w+=|$)").matcher(attributes);
        while (m.find()) {
            attr.put(m.group(1), m.group(2));
        }
        return attr;
    }


    public static void main(String[] args) {
        String clientId = "test&a15lHjvsGaU|securemode=2,signmethod=hmacsha1,ext=1,timestamp=1649835394513";
        String s = clientId.split("\\|")[1];
        System.out.println(s);
        System.out.println(JSON.toJSONString(getAttributes(s)));

    }
}

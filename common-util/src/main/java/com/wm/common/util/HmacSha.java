package com.wm.common.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;
import java.util.Map;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-05-24 15:27
 */
@Slf4j
public class HmacSha {

    /**
     *
     * @param params  key
     * @param secret   secret
     * @param signMethod  hmacsha1  hmacsha256
     * @return
     */
    private String hmacSign(Map<String, String> params, String secret, String signMethod) {
        if (params != null && null!=secret&&!secret.equals("")) {
            String[] sortedKeys = (String[])params.keySet().toArray(new String[0]);
            Arrays.sort(sortedKeys);
            StringBuilder canonicalizedQueryString = new StringBuilder();
            String[] var5 = sortedKeys;
            int var6 = sortedKeys.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                String key = var5[var7];
                if (!"sign".equalsIgnoreCase(key)) {
                    canonicalizedQueryString.append(key).append((String)params.get(key));
                }
            }
            try {
                SecretKey secretKey = new SecretKeySpec(secret.getBytes("utf-8"), signMethod);
                Mac mac = Mac.getInstance(secretKey.getAlgorithm());
                mac.init(secretKey);
                byte[] data = mac.doFinal(canonicalizedQueryString.toString().getBytes("utf-8"));
                return bytesToHexString(data);
            } catch (Exception var9) {
                var9.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public static final String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        for(int i = 0; i < bArray.length; ++i) {
            String sTemp = Integer.toHexString(255 & bArray[i]);
            if (sTemp.length() < 2) {
                sb.append(0);
            }
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

}
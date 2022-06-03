package com.wm.common.util;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-04-11 14:04
 */
public class MyStringUtils {

    //Map<String, String> paramMaps = Splitter.on(",").withKeyValueSeparator("=").split(cliendArray[1]);


    public String random(){
       // RandomStringUtil
        return null;
    }



    public static String parse(String content, Map<String, String> kvs) {
        String pattern = "\\$\\{(.*?)}";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(content);
        StringBuffer sr = new StringBuffer();
        while (m.find()) {
            String group = m.group();
            m.appendReplacement(sr, kvs.get(group));
        }
        m.appendTail(sr);
        return sr.toString();
    }


}

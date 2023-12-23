package com.wm.test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

//编程实现统计某个目录下，有多少种文件，分别列表显示
public class TestFile {
    public static Map<String,Integer> map = new HashMap<String,Integer>();
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        show(new File("C:\\Users\\Administrator\\Desktop\\sl-iot\\paperless2\\paperless\\classes"));
        map.forEach((k,v) ->{
            System.out.printf("%s=%d\n",k,v);
        });

    }
    public static void show(File dir) {
        if(dir.isDirectory()) {
            File[] fs = dir.listFiles();
            for(File f : fs) {
                if(f.isDirectory()) {
                    show(f);
                }else if(f.isFile()) {
                    String n = f.getName();
                    int pos = n.lastIndexOf(".");
                    String ext = pos == -1 ? "未知文件" : n.substring(pos+1);
                    if(map.containsKey(ext)) {
                        map.put(ext, map.get(ext)+ 1);
                    }else {
                        map.put(ext, 1);
                    }
                }
            }
        }
    }

}
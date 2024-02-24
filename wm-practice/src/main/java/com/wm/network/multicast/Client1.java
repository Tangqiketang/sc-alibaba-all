package com.wm.network.multicast;

import java.io.*;
import java.net.*;

/**
 * https://juejin.cn/post/7028097493862875172
 */
public class Client1 {

    public static void main(String[] args) {
        MulticastSocket ms=null;
        InetAddress group=null;
        try{
            ms=new MulticastSocket(Server1.port);
            //只有发送到这个组播组里面的数据才会接收  224.0.0.0~239.255.255.255
            group=InetAddress.getByName(Server1.ip);
            ms.joinGroup(group);
            byte[] buffer=new byte[8194];
            System.out.println("接收数据包启动！（启动时间：）" + new java.util.Date() + ")");
            while(true) {
                DatagramPacket dgp=new DatagramPacket(buffer,buffer.length);
                ms.receive(dgp);
                String s = new String(dgp.getData(), 0, dgp.getLength());
                System.out.println(s);
            }
        }catch(IOException e) {
            e.printStackTrace();
        } finally {
            if(ms!=null) {
                try{
                    ms.leaveGroup(group);
                    ms.close();
                }catch(IOException e) {
                }
            }
        }
    }

}

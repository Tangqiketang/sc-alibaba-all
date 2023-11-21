package com.wm.network.broadcast;

import java.io.*;
import java.net.*;

public class Client1 {

    public static void main(String[] args) {
        MulticastSocket ms=null;
        InetAddress group=null;
        try{
            ms=new MulticastSocket(10000);
            group=InetAddress.getByName("234.1.2.3");
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

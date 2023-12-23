package com.wm.network.multicast;

import java.io.IOException;
import java.net.*;

public class Server1 {

    public static void main(String[] args) throws Exception{
        int port=20000;
        MulticastSocket ms=null;
        InetAddress group=null;
        System.out.println("服务器监听......\n");
        try{
            ms=new MulticastSocket();
            group=InetAddress.getByName("234.1.2.4");
            ms.joinGroup(group);
            byte[] bb=new byte[8194];
            System.out.println("发送数据包启动！（启动时间：）" + new java.util.Date() + ")");
            while(true) {
                String message = "Hello------你好！！！！！" + new java.util.Date();
                byte[] buffer = message.getBytes();
                DatagramPacket dp=new DatagramPacket( buffer, buffer.length,group,port);
                ms.send(dp);
                Thread.sleep(5000);
            }
        }catch(IOException e)
        {
            e.printStackTrace();
        }
        finally{
            if(ms!=null)
            {
                try{
                    ms.leaveGroup(group);
                    ms.close();
                }
                catch(IOException e)
                {

                }
            }
        }
    }

}

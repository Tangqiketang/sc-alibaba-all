package com.wm.network.multicast;

import java.io.IOException;
import java.net.*;

public class Server1 {

    public static int port = 39030;
    public static String ip = "239.254.50.123";

    //private static int port = "20000";
    //private static String ip = "234.1.2.4";

    /**
     * 1.无线路由的情况下组播，客户端无法接收到
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception{
        MulticastSocket ms=null;
        InetAddress group=null;
        System.out.println("服务器监听......\n");
        try{
            ms=new MulticastSocket();
            group=InetAddress.getByName(ip);
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

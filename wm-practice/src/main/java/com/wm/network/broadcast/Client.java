package com.wm.network.broadcast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;

/**
 * @author wangmin
 * @create 2023-12-23 12:44
 */
public class Client {

    public static void main(String[] args) throws Exception{
        DatagramSocket reveSocket = new DatagramSocket(4445);
        new Thread(new Rece3(reveSocket)).start();
    }


    static class Rece3 implements Runnable {
        private DatagramSocket socket;
        public Rece3(DatagramSocket socket) {
            this.socket = socket;
        }
        @Override
        public void run() {
            try {
                while(true) {
                    byte[] buff = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(buff,buff.length);
                    socket.receive(packet);
                    String ip = packet.getAddress().getHostAddress();
                    String data = new String(packet.getData(),0,packet.getLength());
                    if("bye".equals(data)) {
                        System.out.println(ip+"离开了");
                    }
                    System.out.println(ip+"说"+data);
                    //回复
                    byte[] msg = "我收到了".getBytes(StandardCharsets.UTF_8);
                    DatagramPacket datagramPacket = new DatagramPacket(msg, msg.length, packet.getAddress(), packet.getPort());
                    socket.send(datagramPacket);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}

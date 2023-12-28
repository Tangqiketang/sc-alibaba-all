package com.wm.network.broadcast;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

/**
 * @author wangmin
 * @create 2023-12-21 18:00
 */
public class Server {
    private static DatagramSocket socket = null;

    public static void main(String[] args) throws Exception{
        System.out.println("获取到广播地址为:"+getLocalBroadCast());
        System.out.println("遍历获取到的广播地址为:"+JSON.toJSONString(listAllBroadcastAddresses()));
        //255.255.255.255和192.168.0.255的区别
        while (true){
            broadcast("Hello--wm", InetAddress.getByName("192.168.0.255"),4445);
            Thread.sleep(1000);
        }
    }

    /**
     * 发送广播地址
     * @param broadcastMessage
     * @param address
     * @throws IOException
     */
    public static void broadcast(String broadcastMessage, InetAddress address,Integer port) throws IOException {
        socket = new DatagramSocket();
        socket.setBroadcast(true);

        byte[] buffer = broadcastMessage.getBytes();

        DatagramPacket packet
                = new DatagramPacket(buffer, buffer.length, address, port);
        socket.send(packet);
        socket.close();
    }


    /**
     * 获取本机广播地址，并自动区分Windows还是Linux操作系统
     * 每一个网段都有一个广播地址，其格式为 xxx.xxx.xxx.255 的形式。计算方式如下：
     * 网络地址 = IP地址 & 子网掩码
     * 广播地址 = 网络地址 | （~子网掩码）
     * @return String
     */
    public static String getLocalBroadCast(){
        String broadCastIp = null;
        try {
            Enumeration<?> netInterfaces = (Enumeration<?>) NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) netInterfaces.nextElement();
                if (!netInterface.isLoopback()&& netInterface.isUp()) {
                    List<InterfaceAddress> interfaceAddresses = netInterface.getInterfaceAddresses();
                    for (InterfaceAddress interfaceAddress : interfaceAddresses) {
                        //只有 IPv4 网络具有广播地址，因此对于 IPv6 网络将返回 null。
                        if(interfaceAddress.getBroadcast()!= null){
                            broadCastIp =interfaceAddress.getBroadcast().getHostAddress();

                        }
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return broadCastIp;
    }

    /**
     * 获取到每个网卡上的广播地址
     * @return
     * @throws SocketException
     */
    public static  List<InetAddress> listAllBroadcastAddresses() throws SocketException {
        List<InetAddress> broadcastList = new ArrayList<>();
        Enumeration<NetworkInterface> interfaces
                = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();

            if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                continue;
            }

            networkInterface.getInterfaceAddresses().stream()
                    .map(a -> a.getBroadcast())
                    .filter(Objects::nonNull)
                    .forEach(broadcastList::add);
        }
        return broadcastList;
    }
}

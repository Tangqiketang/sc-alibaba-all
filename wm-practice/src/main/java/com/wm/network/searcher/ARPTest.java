package com.wm.network.searcher;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangmin
 * @create 2024-01-11 11:55
 */
public class ARPTest {

    private static Map<String,String> ipMac = new HashMap<>();

    public static void main(String[] args) throws Exception{
        String ip = "192.168.0.28";
        String command = "arp -a ";
        Process process = Runtime.getRuntime().exec(command);
        //process.waitFor(1, TimeUnit.SECONDS);
        process.waitFor();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
           // if (line.contains(ip)) {
                String[] ipStr = line.split("\\s+");
                if(ipStr!=null && ipStr.length>2){
                    String ipget = ipStr[1];
                    String mac = ipStr[2];
                    System.out.println("ip:"+ipget+"  mac:"+mac);
                }

                //break;
           // }
        }
        reader.close();

    }

/*    public static void main(String[] args) throws Exception{
        try {
            Process process = Runtime.getRuntime().exec("arp -a");

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("请求方IP地址")) {
                    int index = line.indexOf("请求方IP地址") + "请求方IP地址".length() + 2;
                    String macAddress = line.substring(index, index + 17);
                    System.out.println("请求方的MAC地址为：" + macAddress);
                    break;
                }
            }

            reader.close();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }*/

}

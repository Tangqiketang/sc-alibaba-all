package com.wm.druid;

import com.alibaba.druid.filter.config.ConfigTools;
import com.alibaba.druid.util.DruidPasswordCallback;

public class DruidDecode extends DruidPasswordCallback {

    public static void main(String[] args) throws Exception {
        String publickey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJ4KwUxB3/xlYCG/kNWiLhX/xKbraf/nkC9mCJ3hLtMki+uIVPZNW2smKuESYfMATDn7qECOMCzac+zrBv/cbQ0CAwEAAQ==";
        String password = "QA9ThCV+R5Uycywxi/DNtUBpO1e5FUdM92LWR8lrZGRTLjbmefaIush2g/ms6TWprHdcgXvjjdCIm/8/fn08Dw==";
        String pwd = ConfigTools.decrypt(publickey, password);

        System.out.println(pwd);


        String passwd2 = ConfigTools.encrypt(publickey,"PV8NQ@RSkVHZ");
        System.out.println(passwd2);
    }

}

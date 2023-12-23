package com.wm.base;

/**
 * @author wangmin
 * @create 2023-10-17 10:36
 */
public class TestFinal {

/*    public static void main(String[] args) {
        //fianl 对象，不能修改引用，可以修改内容
        final Device device = new Device("1","2","3",9.9d);
        device.setPrice(999999d);

        System.out.println(device);
    }*/

    public static void main(String[] args) {
        SonClass sonClass = new SonClass();
        FatherClass fatherClass = new FatherClass();
        fatherClass.setName("father");
        fatherClass.setAge(50);
        sonClass.setName("son");
        sonClass.setAge(18);
        sonClass.setFair("头发");
        System.out.println("son："+sonClass.toString());
        System.out.println(fatherClass.toString());
    }


}

package com.demo.jdk8.funinterface;

/**
 * @author LuoTao
 * @email taomee517@qq.com
 * @date  2018/12/26
 * @time  9:51
 */
public class FuncApp {
    public static void main(String[] args) {
        MyService service = new MyService();
        service.intro();
        String tag = service.getTag();
        System.out.println("MyService类的Tag是:" + tag);
    }
}

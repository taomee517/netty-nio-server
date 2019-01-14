package com.demo.jdk8.funinterface;

/**
 * @author LuoTao
 * @email taomee517@qq.com
 * @date 2018/12/26
 * @time 9:48
 */
public class MyService implements IFunctionalService2 {
    private static final String TAG = "fun";

    @Override
    public String getTag() {
        return TAG;
    }

//    @Override
//    public void intro() {
//        System.out.println("重写的intro方法!");
//    }
}

package com.demo.jdk8.funinterface;

/**
 * @author LuoTao
 * @email taomee517@qq.com
 * @date 2018/12/26
 * @time 9:45
 */
@FunctionalInterface
public interface IFunctionalService1 {

    String getTag();

    static void intro(){
        System.out.println("带方法的接口1");
    }

    static void staticMethod(){
        System.out.println("接口1的静态方法");
    }

    default void defaultMethod(){
        System.out.println("接口1的默认方法");
    }
}

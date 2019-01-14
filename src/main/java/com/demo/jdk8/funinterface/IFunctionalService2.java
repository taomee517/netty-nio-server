package com.demo.jdk8.funinterface;

/**
 * @author LuoTao
 * @email taomee517@qq.com
 * @date 2018/12/26
 * @time 9:45
 */
@FunctionalInterface
public interface IFunctionalService2 {

    String getTag();

    default void intro(){
        System.out.println("带方法的接口2");
    }

    static void staticMethod(){
        System.out.println("接口2的静态方法");
    }

    default void defaultMethod(){
        System.out.println("接口2的默认方法");
    }
}

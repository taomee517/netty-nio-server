package com.demo.nio.share.multi;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Nio多线程实例
 *
 * @Author luotao
 * @E-mail taomee517@qq.com
 * @Date 2019\3\1 0001 21:04
 */
public class NioMultiDemo {
    public static void main(String[] args) {
        try {
            ExecutorService boss = Executors.newFixedThreadPool(1);
            Reactor reactor = new Reactor(8001);
            boss.execute(reactor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

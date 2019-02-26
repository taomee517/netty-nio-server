package com.demo.netty.basic;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

import java.util.Queue;

/**
 * EventLoopGroup源码分析
 *
 * @Author luotao
 * @E-mail taomee517@qq.com
 * @Date 2019\2\23 0023 10:26
 */
public class EventLoopGroupDemo {
    public static void main(String[] args) {
        EventLoopGroup boss = new NioEventLoopGroup(1);
        System.out.println("boss可执行的线程数：" + ((NioEventLoopGroup) boss).executorCount());
        EventLoopGroup worker = new NioEventLoopGroup(0);
        System.out.println("worker可执行的线程数：" + ((NioEventLoopGroup) worker).executorCount());
        worker.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("执行类:" + worker.getClass().getName());
            }
        });

    }
}


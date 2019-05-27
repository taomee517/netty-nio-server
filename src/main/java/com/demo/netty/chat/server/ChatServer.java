package com.demo.netty.chat.server;

import com.demo.netty.basic.constant.DefaultValue;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 模拟聊天室服务器
 *
 * @Author luotao
 * @E-mail taomee517@qq.com
 * @Date 2019\1\27 15:59
 */
public class ChatServer {
    public static void main(String[] args) {
        EventLoopGroup boss = new NioEventLoopGroup(DefaultValue.DEFAULT_BOSS_THREAD);
        EventLoopGroup worker = new NioEventLoopGroup(DefaultValue.DEFAULT_WORKER_THREAD);
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss,worker).channel(NioServerSocketChannel.class).childHandler(new ChatServerInitializer());
            ChannelFuture future = bootstrap.bind(DefaultValue.DEFAULT_TEST_PORT).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}

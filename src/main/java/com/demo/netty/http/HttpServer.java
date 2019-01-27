package com.demo.netty.http;

import com.demo.future.FutureTaskApp;
import com.demo.netty.constant.DefaultValue;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * Netty Http案例服务器端
 *
 * @author luotao
 * @e-mail taomee517@qq.com
 * @date 2019\1\27 0027
 * @time 13:01
 */
public class HttpServer {
    public static void main(String[] args) {
        EventLoopGroup boss = new NioEventLoopGroup(DefaultValue.DEFAULT_BOSS_THREAD);
        EventLoopGroup worker = new NioEventLoopGroup(DefaultValue.DEFAULT_WORKER_THREAD);
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss,worker).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    ChannelPipeline pipeline = socketChannel.pipeline();
                    pipeline.addLast(new HttpServerCodec());
                    pipeline.addLast(new HttpHandler());
                }
            });
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

package com.demo.netty.codec.lengthbase;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import static com.demo.netty.basic.constant.DefaultValue.DEFAULT_TEST_IP;
import static com.demo.netty.basic.constant.DefaultValue.DEFAULT_TEST_PORT;


public class Client {
    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new MsgDecoder());
                pipeline.addLast(new MsgEncoder());
                pipeline.addLast(new ActiveHandler());
            }
        });
        try {
            ChannelFuture future = b.connect(DEFAULT_TEST_IP,DEFAULT_TEST_PORT).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}

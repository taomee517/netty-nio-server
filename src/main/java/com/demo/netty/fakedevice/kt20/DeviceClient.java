//package com.demo.netty.fakedevice.kt20;
//
//import com.demo.netty.basic.constant.DefaultValue;
//import com.demo.netty.fakedevice.kt20.handler.DeviceInitializer;
//import io.netty.bootstrap.Bootstrap;
//import io.netty.channel.ChannelFuture;
//import io.netty.channel.EventLoopGroup;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.nio.NioSocketChannel;
//
///**
// * 模拟聊天室客户端
// *
// * @Author luotao
// * @E-mail taomee517@qq.com
// * @Date 2019\1\27 0027 16:44
// */
//public class DeviceClient {
//    public static void main(String[] args) throws Exception{
//        EventLoopGroup eventLoopGroup = new NioEventLoopGroup(DefaultValue.DEFAULT_WORKER_THREAD);
//        try {
//            Bootstrap bootstrap = new Bootstrap();
//            bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).handler(new DeviceInitializer());
//            ChannelFuture future = bootstrap.connect("192.168.2.61", 2120).sync();
//
//            future.channel().closeFuture().sync();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } finally {
//            eventLoopGroup.shutdownGracefully();
//        }
//    }
//}

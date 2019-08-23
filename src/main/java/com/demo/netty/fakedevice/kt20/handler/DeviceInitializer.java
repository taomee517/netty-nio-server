package com.demo.netty.fakedevice.kt20.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * 聊天室客户端初始化器
 *
 * @Author luotao
 * @E-mail taomee517@qq.com
 * @Date 2019\1\27 0027 16:02
 */
public class DeviceInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new KT20Encoder());
        pipeline.addLast(new KT20Decoder());
        pipeline.addLast(new DeviceHandler());
    }
}

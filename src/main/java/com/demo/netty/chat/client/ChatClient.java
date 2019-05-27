package com.demo.netty.chat.client;

import com.demo.netty.basic.constant.DefaultValue;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * 模拟聊天室客户端
 *
 * @Author luotao
 * @E-mail taomee517@qq.com
 * @Date 2019\1\27 0027 16:44
 */
public class ChatClient {
    public static void main(String[] args) throws Exception{
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup(DefaultValue.DEFAULT_WORKER_THREAD);
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).handler(new ChatClientInitializer());
            Channel channel = bootstrap.connect(DefaultValue.DEFAULT_TEST_IP,DefaultValue.DEFAULT_TEST_PORT).sync().channel();
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while (true){
                //'/r/n'是lineBasedFrameCodec的delimiter
                channel.writeAndFlush(br.readLine() + "\r\n");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
}

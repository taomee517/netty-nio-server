package com.demo.netty.udp;

import com.demo.netty.idlecheck.IdleHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author 罗涛
 * @title UdpClient
 * @date 2020/11/24 15:39
 */
public class UdpClient {
    private Bootstrap bootstrap;
    public NioEventLoopGroup workerGroup;
    public static Channel channel;

    public void start() throws Exception{
        try {
            bootstrap = new Bootstrap();
            workerGroup = new NioEventLoopGroup();
            bootstrap.group(workerGroup)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new ChannelInitializer<NioDatagramChannel>() {
                        @Override
                        protected void initChannel(NioDatagramChannel ch)throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new IdleStateHandler(0,0,10, TimeUnit.SECONDS));
                            pipeline.addLast(new GkGmHandler());
                        }
                    });
            channel = bootstrap.bind(randomPort()).sync().channel();
            channel.closeFuture().await(1000);
        } finally {
        	workerGroup.shutdownGracefully();
        }
    }

    public Channel getChannel(){
        return channel;
    }


    private int randomPort(){
        return new Random().nextInt(35000) + 30000;
    }
}

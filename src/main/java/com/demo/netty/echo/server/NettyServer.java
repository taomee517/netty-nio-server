package com.demo.netty.echo.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author LuoTao
 * @email taomee517@qq.com
 * @date 2018/12/26
 * @time 12:31
 */
@Slf4j
public class NettyServer {
    public static void main(String[] args) {
        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup();
        List<Integer> ports = Arrays.asList(8001);
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            final MyChannelHandler echoHandler = new MyChannelHandler();
            bootstrap.group(boss,worker).channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch){
                            ChannelPipeline pipeline = ch.pipeline();
//                            pipeline.addLast(new MyDecoder());
                            pipeline.addLast(echoHandler);
                        }
            });
            Collection<Channel> channels = new ArrayList<>(ports.size());
            //绑定多个端口
            for (int port : ports) {
                Channel serverChannel = bootstrap.bind(port).sync().channel();
                channels.add(serverChannel);
                log.info("服务运行成功，监听端口:{}", port);
            }
            //关闭多个通道
            for (Channel ch : channels) {
                ch.closeFuture().sync();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}

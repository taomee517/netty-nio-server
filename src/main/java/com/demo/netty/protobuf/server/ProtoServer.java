package com.demo.netty.protobuf.server;

import com.demo.netty.protobuf.handler.ServerProtoHandler;
import com.demo.netty.protobuf.pojo.RichManProto;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProtoServer {
    public static void main(String[] args) throws Exception {
        int port = 8001;
        new ProtoServer().bind(port);
    }

    public void bind(int port)throws Exception{
        // 配置服务端的NIO线程组
        // boss仅配置一个线程，worker配置默认值 可用核心数*2
        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss,worker).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO)).childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                    socketChannel.pipeline().addLast(new ProtobufDecoder(RichManProto.RichMan.getDefaultInstance()));
                    socketChannel.pipeline().addLast(new ServerProtoHandler());
                }
            });

            log.info("=====init start=====");
            // 绑定端口，同步等待成功
            ChannelFuture task = bootstrap.bind(port).sync();
            // 等待服务端监听端口关闭
            task.channel().closeFuture().sync();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}

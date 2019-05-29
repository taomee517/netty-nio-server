package com.demo.netty.protobuf.personinfo.client;

import com.demo.netty.protobuf.personinfo.handler.ClientProtoHandler;
import com.demo.netty.protobuf.personinfo.handler.ServerProtoHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

public class ProtoClient {
    public static void main(String[] args) throws Exception {
        String ip = "127.0.0.1";
        int port = 8001;
        new ProtoClient().connect(ip, port);
    }

    public void connect(String ip, int port)throws Exception{
        // 配置服务端的NIO线程组
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(worker).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
                    socketChannel.pipeline().addLast(new ProtobufEncoder());
                    socketChannel.pipeline().addLast(new ClientProtoHandler());
                }
            });

            // 绑定端口，同步等待成功
            ChannelFuture task = bootstrap.connect(ip, port).sync();
            // 等待服务端监听端口关闭
            task.channel().closeFuture().sync();
        } finally {
            worker.shutdownGracefully();
        }
    }
}

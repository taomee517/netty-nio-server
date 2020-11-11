//package com.demo.netty.codec.otudelimiter;
//
//import io.netty.bootstrap.ServerBootstrap;
//import io.netty.channel.*;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.SocketChannel;
//import io.netty.channel.socket.nio.NioServerSocketChannel;
//import io.netty.handler.logging.LogLevel;
//import io.netty.handler.logging.LoggingHandler;
//
//import static com.demo.netty.basic.constant.DefaultValue.DEFAULT_TEST_PORT;
//
//public class Server {
//    public static void main(String[] args) {
//        EventLoopGroup boss = new NioEventLoopGroup(1);
//        EventLoopGroup worker = new NioEventLoopGroup();
//        try {
//            ServerBootstrap bootstrap = new ServerBootstrap();
//            bootstrap.group(boss,worker).channel(NioServerSocketChannel.class)
//                    .option(ChannelOption.SO_BACKLOG, 100)
//                    .handler(new LoggingHandler(LogLevel.INFO))
//                    .childHandler(new ChannelInitializer<SocketChannel>() {
//                        @Override
//                        protected void initChannel(SocketChannel socketChannel) throws Exception {
//                            ChannelPipeline pipeline = socketChannel.pipeline();
//                            pipeline.addLast(new ProtocolCodec());
//                        }
//                    });
//            ChannelFuture future = bootstrap.bind(DEFAULT_TEST_PORT).sync();
//            future.channel().closeFuture().sync();
//        } catch (Exception e){
//            e.printStackTrace();
//        } finally {
//            boss.shutdownGracefully();
//            worker.shutdownGracefully();
//        }
//    }
//}

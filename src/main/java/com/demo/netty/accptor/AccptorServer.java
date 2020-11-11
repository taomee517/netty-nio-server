//package com.demo.netty.accptor;
//
//import com.blackTea.common.constants.ClientTypeEnum;
//import com.demo.netty.accptor.handler.ClientLogoutHandler;
//import com.demo.netty.accptor.handler.LoginTimeoutHandler;
//import com.demo.netty.accptor.handler.NettyCodec;
//import com.demo.netty.accptor.handler.NettyServerMsgHandler;
//import io.netty.bootstrap.ServerBootstrap;
//import io.netty.channel.Channel;
//import io.netty.channel.ChannelInitializer;
//import io.netty.channel.ChannelPipeline;
//import io.netty.channel.EventLoopGroup;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.SocketChannel;
//import io.netty.channel.socket.nio.NioServerSocketChannel;
//import io.netty.handler.logging.LogLevel;
//import io.netty.handler.logging.LoggingHandler;
//import io.netty.handler.timeout.IdleStateHandler;
//import lombok.extern.slf4j.Slf4j;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
//@Slf4j
//public class AccptorServer {
//    public static void main(String[] args) {
//        EventLoopGroup boss = new NioEventLoopGroup(4);
//        EventLoopGroup worker = new NioEventLoopGroup();
//        List<Integer> ports = Arrays.asList(ClientTypeEnum.OTU.getPort(), ClientTypeEnum.BSJ.getPort(), ClientTypeEnum.GL500.getPort());
//        try{
//            ServerBootstrap bootstrap = new ServerBootstrap();
//            bootstrap.group(boss,worker).channel(NioServerSocketChannel.class)
//                    .handler(new LoggingHandler(LogLevel.DEBUG))
//                    .childHandler(new ChannelInitializer<SocketChannel>() {
//                        @Override
//                        protected void initChannel(SocketChannel ch){
//                            ChannelPipeline pipeline = ch.pipeline();
//                            pipeline.addLast(new IdleStateHandler(600,600,0, TimeUnit.SECONDS));
//                            pipeline.addLast(new NettyCodec());
//                            //5秒不登录,就直接踢掉连接
//                            pipeline.addLast("loginTimeOuthandler", new LoginTimeoutHandler(600));
//                            pipeline.addLast("handler", new NettyServerMsgHandler());
//                            pipeline.addLast("logoutHandler", new ClientLogoutHandler());
//                        }
//                    });
//            Collection<Channel> channels = new ArrayList<>(ports.size());
//            //绑定多个端口
//            for (int port : ports) {
//                Channel serverChannel = bootstrap.bind(port).sync().channel();
//                channels.add(serverChannel);
//                log.info("服务运行成功，监听{}设备上线端口:{}",ClientTypeEnum.getEnumByPort(port), port);
//            }
//            //关闭多个通道
//            for (Channel ch : channels) {
//                ch.closeFuture().sync();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }finally {
//            boss.shutdownGracefully();
//            worker.shutdownGracefully();
//        }
//    }
//}

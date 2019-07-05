package com.demo.netty.component.idlestate;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

import static com.demo.netty.basic.constant.DefaultValue.DEFAULT_TEST_IP;
import static com.demo.netty.basic.constant.DefaultValue.DEFAULT_TEST_PORT;

@Slf4j
public class IdleStateClient {
    public static void main(String[] args) {
        connect(DEFAULT_TEST_IP,DEFAULT_TEST_PORT);
    }


    public static void connect(String ip, int port) {
        EventLoopGroup worker = new NioEventLoopGroup();
        ChannelFuture future = null;
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(worker).channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                            ChannelPipeline pipeline = nioSocketChannel.pipeline();
                            pipeline.addLast(new IdleStateHandler(0,4,0, TimeUnit.SECONDS));
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(new MyIdleClientHandler());
                        }
                    });
            future = bootstrap.connect(ip,port).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //channel断开后，就关闭
//            worker.shutdownGracefully();

            //连接断开后，尝试重连
            if (null != future) {
                if (future.channel() != null && future.channel().isOpen()) {
                    future.channel().close();
                }
            }
            log.info("准备重连");
            connect(ip,port);
            log.info("重连成功");
        }

    }
}

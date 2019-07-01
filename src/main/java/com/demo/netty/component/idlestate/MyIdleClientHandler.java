package com.demo.netty.component.idlestate;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyIdleClientHandler extends ChannelInboundHandlerAdapter {
    public static final String HEART_BEAT_MSG = "Hello,Server!";
    public static final int RETRY_TIMES = 3;

    private int retry = 0;

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        IdleStateEvent event = (IdleStateEvent)evt;
        if(event.equals(IdleStateEvent.WRITER_IDLE_STATE_EVENT)){
            if (retry<RETRY_TIMES) {
                retry++;
                log.info("发生写超时事件,retry = {}", retry);
                ctx.channel().writeAndFlush(HEART_BEAT_MSG);
            }
//            else {
//                ctx.channel().close();
//            }
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("收到服务器消息：{}", msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("连接断开");
        ctx.channel().close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("MyIdleClientHandler发生异常：{}", cause);
        ctx.channel().close();
    }



}

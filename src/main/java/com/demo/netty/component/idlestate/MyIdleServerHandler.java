package com.demo.netty.component.idlestate;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyIdleServerHandler extends ChannelInboundHandlerAdapter {
    public static final String HB_ACK = "Hello,Client!";

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        IdleStateEvent event = (IdleStateEvent)evt;
        if(event.equals(IdleStateEvent.READER_IDLE_STATE_EVENT)){
            log.info("发生读超时事件");
            ctx.channel().close();
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("收到客户端消息：{}", msg);
        ctx.channel().writeAndFlush(HB_ACK);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("连接断开");
        ctx.channel().close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("MyIdleServerHandler发生异常：{}", cause);
        ctx.channel().close();
    }

}

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
        /**通常服务端要对心跳包做出响应，其实给客户端最好的回复就是“不回复”，这样可以减轻服务端的压力。
         * 假如有10w个空闲Idle的连接，那么服务端光响应心跳，都需要消耗大量资源！
         * 那么怎么才能告诉客户端它还活着呢，其实很简单，因为5s服务端都会收到来自客户端的心跳信息，
         * 如果10秒，甚至15秒内都收不到客户端的心跳，服务端可以认为客户端挂了，可以close链路
         */
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

package com.demo.netty.echo.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

@Slf4j
@ChannelHandler.Sharable
public class MyChannelHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("channel info:{}", ctx.channel());
        ByteBuf buf = (ByteBuf)msg;
        String data = buf.toString(Charset.forName("utf-8"));

//        String data = (String)msg;
        log.info("收到消息：{}", data);
        ctx.writeAndFlush(msg);
}

//    @Override
//    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        ctx.flush();
//    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端：{} 断开连接", ctx.channel());
    }
}

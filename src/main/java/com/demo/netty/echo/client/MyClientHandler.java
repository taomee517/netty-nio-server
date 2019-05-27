package com.demo.netty.echo.client;

import com.demo.netty.basic.constant.DefaultValue;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;

public class MyClientHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String msg = DefaultValue.DEFAULT_FIRST_MSG;
        ByteBuf buf = Unpooled.buffer(msg.length());
        for(int i=0;i<msg.length();i++){
            buf.writeByte((byte)msg.charAt(i));
        }
        ctx.writeAndFlush(buf);
        System.out.println("向服务器发送消息：" + msg);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf)msg;
        String data = buf.toString(Charset.forName("utf-8"));
        System.out.println("收到消息：" + data);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
       ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

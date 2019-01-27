package com.demo.netty.chat.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.net.SocketAddress;

/**
 * 聊天室服务器客户端消息处理类
 *
 * @Author luotao
 * @E-mail taomee517@qq.com
 * @Date 2019\1\27 0027 16:11
 */
public class ChatHandler extends SimpleChannelInboundHandler<String> {
    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        Channel channel = channelHandlerContext.channel();
        channels.forEach(ch ->{
            SocketAddress addr = channel.remoteAddress();
            String content;
            if(channel.equals(ch)){
                content = "自己：" + s + "\n";
            }else{
                content = "客户端" + addr + "发出消息：" + s + "\n";
            }
            ch.writeAndFlush(content);
        });
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        SocketAddress addr = channel.remoteAddress();
        String content = "客户端：" + addr + "加入\n";
        channels.writeAndFlush(content);
        channels.add(channel);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        SocketAddress addr = channel.remoteAddress();
        String content = "客户端：" + addr + "离开\n";
        channels.writeAndFlush(content);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        SocketAddress addr = channel.remoteAddress();
        String content = "客户端：" + addr + "上线";
        System.out.println(content);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        SocketAddress addr = channel.remoteAddress();
        String content = "客户端：" + addr + "离线";
        System.out.println(content);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

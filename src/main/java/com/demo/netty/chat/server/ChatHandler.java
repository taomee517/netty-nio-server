package com.demo.netty.chat.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

import java.net.SocketAddress;

/**
 * 聊天室服务器客户端消息处理类
 *
 * @Author luotao
 * @E-mail taomee517@qq.com
 * @Date 2019\1\27 0027 16:11
 */
@Slf4j
public class ChatHandler extends SimpleChannelInboundHandler<String> {
    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        log.info("Channel Read!");
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
        log.info("handlerAdded");
        Channel channel = ctx.channel();
        SocketAddress addr = channel.remoteAddress();
        String content = "客户端：" + addr + "加入\n";
        channels.writeAndFlush(content);
        channels.add(channel);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        log.info("handlerRemoved");
        Channel channel = ctx.channel();
        SocketAddress addr = channel.remoteAddress();
        String content = "客户端：" + addr + "离开\n";
        channels.writeAndFlush(content);
        //验证handlerRemoved自动删除channel
        log.info("channel的个数：{}", channels.size());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("channelActive");
        Channel channel = ctx.channel();
        SocketAddress addr = channel.remoteAddress();
        String content = "客户端：" + addr + "上线";
        log.info(content);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("channelInactive");
        Channel channel = ctx.channel();
        SocketAddress addr = channel.remoteAddress();
        String content = "客户端：" + addr + "离线";
        log.info(content);
        ctx.fireChannelInactive();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("exceptionCaught");
        ctx.close();
    }
}

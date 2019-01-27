package com.demo.netty.chat.server;

import com.demo.netty.constant.DefaultValue;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * 聊天室服务器初始化器
 *
 * @Author luotao
 * @E-mail taomee517@qq.com
 * @Date 2019\1\27 0027 16:02
 */
public class ChatServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new DelimiterBasedFrameDecoder(DefaultValue.MSG_MAX_SIZE, Delimiters.lineDelimiter()));
        pipeline.addLast(new StringDecoder(DefaultValue.DEFAULT_CHARSET));
        pipeline.addLast(new StringEncoder(DefaultValue.DEFAULT_CHARSET));
        pipeline.addLast(new ChatHandler());

    }
}

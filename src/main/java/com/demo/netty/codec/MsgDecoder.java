package com.demo.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class MsgDecoder extends ReplayingDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int length = in.readInt();
        byte[] content = new byte[length];
        in.readBytes(content);
        Msg msg = new Msg();
        msg.setLength(length);
        msg.setContent(content);
        out.add(msg);
    }
}

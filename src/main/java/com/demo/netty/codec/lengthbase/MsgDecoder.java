package com.demo.netty.codec.lengthbase;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class MsgDecoder extends ReplayingDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if(in.readableBytes()<4){
            throw new Exception("长度不能小于4");
        }
        int length = in.readInt();
        byte[] content = new byte[length];
        in.readBytes(content);
        Msg msg = new Msg();
        msg.setLength(length);
        msg.setContent(content);
        out.add(msg);
    }
}

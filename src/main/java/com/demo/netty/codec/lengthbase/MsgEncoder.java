package com.demo.netty.codec.lengthbase;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MsgEncoder extends MessageToByteEncoder<Msg> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Msg msg, ByteBuf out) throws Exception {
//        System.out.println("进入encode方法");
        out.writeInt(msg.getLength());
        out.writeBytes(msg.getContent());
    }
}

package com.demo.netty.fakedevice.kt20.handler;

import com.demo.netty.fakedevice.kt20.util.KT20CodecUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class KT20Decoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        String str = KT20CodecUtil.Byte2StringSerialize(in).toUpperCase();
        out.add(str);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("KT20Decoder发生异常：", cause);
        ctx.close();
    }
}

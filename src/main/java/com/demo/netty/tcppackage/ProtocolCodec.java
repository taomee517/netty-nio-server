package com.demo.netty.tcppackage;

import com.fzk.otu.api.upstream.decode.OtuDecoder;
import com.fzk.sdk.common.SessionContext;
import com.fzk.sdk.pojo.BaseDataModel;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ProtocolCodec extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        byte[] msg = new byte[byteBuf.readableBytes()];
        byteBuf.resetReaderIndex();
        byteBuf.readBytes(msg);
        String in = new String(msg,"UTF-8");
        log.info("收到消息：{}", in);
        OtuDecoder decoder = new OtuDecoder();
        BaseDataModel model = decoder.decode(msg, new SessionContext()).get(0);
        String resp = "消息类型：" + model.getMsgType() + "，消息头：" + model.getProtocolType()
                + ",标签：" + model.getProtocolTag() + ",服务器回复内容：" + model.getRespContent();
        channelHandlerContext.writeAndFlush(resp);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("服务端协议解析时发生异常：{}", cause);
        ctx.close();
    }
}

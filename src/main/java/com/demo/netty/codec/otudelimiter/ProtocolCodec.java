package com.demo.netty.codec.otudelimiter;

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

    public static final byte leftWrap = '(';
    public static final byte rightWrap = ')';

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        String in = byte2StringSerialize(byteBuf);
        log.info("收到消息：{}", in);
        OtuDecoder decoder = new OtuDecoder();
        BaseDataModel model = decoder.decode(in.getBytes(), new SessionContext()).get(0);
        String resp = "消息类型：" + model.getMsgType() + "，消息头：" + model.getProtocolType()
                + ",标签：" + model.getProtocolTag() + ",服务器回复内容：" + model.getRespContent();
        channelHandlerContext.writeAndFlush(resp);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("服务端协议解析时发生异常：{}", cause);
        ctx.close();
    }

    /**
     * 这个方法就是在做TCP的拆包操作，根据起始位和结束符来截取我们要的消息内容
     * @param in 数据流
     * @return 第一段报文内容
     * @throws Exception
     */
    public static String byte2StringSerialize(ByteBuf in) throws Exception {
        int length = in.readableBytes();
        if (length < 2) {
            return null;
        }
        int readStart = in.readerIndex();
        int startIndex = -1;
        int endIndex = -1;
        byte currentByte = 0;
        for (int index = 0; index < length; index++) {
            currentByte = in.readByte();
            // 确定起始位
            if (leftWrap == currentByte) {
                startIndex = index;
            // 确定结束位，并读取第一段报文内容，并转为String返回
            } else if (startIndex >= 0 && rightWrap == currentByte) {
                endIndex = index;
                int contentLength = endIndex - startIndex - 1;
                in.readerIndex(readStart + startIndex + 1);
                byte[] result = new byte[contentLength];
                in.readBytes(result, 0, contentLength);
                in.readByte();
                return new String(result, "UTF-8");
            }
        }
        //如果第一位不是开始符 左括号，就调整偏移量
        if (startIndex >= 0) {
            in.readerIndex(readStart + startIndex);
        }
        return null;
    }
}

package com.demo.netty.idlecheck;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.springframework.util.StringUtils;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @author LuoTao
 * @email taomee517@qq.com
 * @date 2019/2/15
 * @time 12:07
 */
public class MsgDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        String text = in.toString(Charset.forName("UTF-8"));
        Message message = new Message();
        String content = text.substring(1,text.lastIndexOf(")"));
        if(StringUtils.isEmpty(content)){
            message.setHb(true);
        }else {
            String[] info = content.split("|");
            String[] tnv = info[1].split(",");
            message.setHb(false);
            message.setHead(info[0]);
            message.setTag(tnv[0]);
            message.setIndex(Integer.parseInt(tnv[1]));
        }
        out.add(message);
    }
}

package com.demo.netty.basic.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class ByteBufDemo {
    public static void main(String[] args) {
        ByteBuf buf = Unpooled.buffer(10);
        String text = "你好！世界";
        byte[] src = text.getBytes();
        for(int i = 0;i<src.length;i++){
            byte b = src[i];
            buf.writeByte(b);
        }
        for (int i = 0;i<buf.writerIndex();i++){
            byte[] bs = {buf.getByte(i)};
            String s = new String(bs);
            System.out.print(s);
        }
        byte[] allBytes = buf.array();
        String bout = "";
        try {
            bout = new String(allBytes,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println();
        String charSet = "utf-8";
        String out = buf.toString(Charset.forName(charSet));
        System.out.println("ToString with "+ charSet.toUpperCase() +":" + out);
        System.out.println(bout);
        System.out.println(allBytes.length);
        System.out.println(text.getBytes().length);
    }
}

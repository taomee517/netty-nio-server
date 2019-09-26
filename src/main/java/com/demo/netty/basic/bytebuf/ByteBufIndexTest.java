package com.demo.netty.basic.bytebuf;

import com.blackTea.util.BytesUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ByteProcessor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ByteBufIndexTest {
    public static final byte SIGN_CODE = 0x7E;
    public static void main(String[] args) {
        ByteBuf buf = Unpooled.buffer(10);
        byte[] bytes = {0x7e, 0x3c, 0x7e};
        for(int i=0; i<bytes.length; i++){
            buf.writeByte(bytes[i]);
        }
        int startSignIndex = buf.forEachByte(new ByteProcessor.IndexOfProcessor(SIGN_CODE));
        if(startSignIndex==-1){
            System.out.println("没有找到起始符");
        }
        //将readerIndex置为起始符下标+1
        //因为起始符结束符是一样的，如果不往后移一位，下次到的还是起始下标
        buf.readerIndex(startSignIndex + 1);

        //找到第一个报文结束符的下标
        int endSignIndex = buf.forEachByte(new ByteProcessor.IndexOfProcessor(SIGN_CODE));
        if(endSignIndex == -1){
            buf.readerIndex(startSignIndex);
            System.out.println("没有找到结束符");
        }
        System.out.println(startSignIndex);
        System.out.println(endSignIndex);

        int length = endSignIndex + 1 - startSignIndex;
        byte[] errMsg = new byte[length];
        for(int i= startSignIndex; i< (endSignIndex + 1); i++){
            int errIndex = i-startSignIndex;
            errMsg[errIndex] = buf.getByte(i);
        }
        log.error("异常消息，有分隔符但长度太短：{}", BytesUtil.bytesToHexShortString(errMsg));


        //将报文内容写入符串，并返回
        byte[] data = new byte[length];
        buf.readerIndex(startSignIndex);
        buf.readBytes(data);
        log.info("最终结果：{}",BytesUtil.bytesToHexString4BSJ(data).toUpperCase());

        log.info("buf = {}", buf);
    }
}

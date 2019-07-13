package com.demo.netty.basic.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ByteProcessor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ByteBufTest {
    public static void main(String[] args) {
        /**
         * HeapBuffer堆缓冲区可以快速分配，当不使用时也可以快速释放。
         * 它还提供了直接访问数组的方法，通过 ByteBuf.array() 来获取 byte[]数据
         */
        ByteBuf buf = Unpooled.buffer(10);
//        ByteBuf buf = Unpooled.directBuffer(10);
        int readerIndex = buf.readerIndex();
        int writerIndex = buf.writerIndex();
        int capacity = buf.capacity();
        boolean isHeap = buf.hasArray();
        log.info("是否直接缓冲：{}", !isHeap);
        log.info("初始化时，readerIndex = {} , writerIndex = {}, capacity = {}", readerIndex, writerIndex, capacity);
        System.out.println("==================================================================");

        String text = "Hello,World!";
        byte[] src = text.getBytes();
        for(int i = 0;i<capacity;i++){
            byte b = src[i];
            buf.writeByte(b);
            readerIndex = buf.readerIndex();
            writerIndex = buf.writerIndex();
            capacity = buf.capacity();
            log.info("写入数据时，readerIndex = {} , writerIndex = {}, capacity = {}", readerIndex, writerIndex, capacity);
        }
        System.out.println("==================================================================");

        byte[] temp = new byte[capacity];
        if(buf.hasArray()){
            temp = buf.array();
            String data = new String(temp);
            log.info("读到的信息：{}", data);
        }
        for(int i=0;i<buf.writerIndex();i++){
            temp[i] = buf.readByte();
            readerIndex = buf.readerIndex();
            writerIndex = buf.writerIndex();
            capacity = buf.capacity();
            log.info("读取数据时，readerIndex = {} , writerIndex = {}, capacity = {}", readerIndex, writerIndex, capacity);
        }
        System.out.println("==================================================================");

        String msg = new String(temp);
        log.info("读到的信息：{}", msg);



        /**
         * 注意通过索引访问时不会推进 readerIndex （读索引）和 writerIndex（写索引）
         * 可以通过 ByteBuf 的 readerIndex(index) 或 writerIndex(index) 来分别推进读索引或写索引
         * 通过readByte 和 writeByte 也会推进读索引或写索引
         */
        buf.resetReaderIndex();
        byte[] cache = new byte[capacity];
        for(int i=0;i<buf.writerIndex();i++){
            cache[i] = buf.getByte(i);
            readerIndex = buf.readerIndex();
            writerIndex = buf.writerIndex();
            capacity = buf.capacity();
            log.info("通过索引getByte读取数据时，readerIndex = {} , writerIndex = {}, capacity = {}", readerIndex, writerIndex, capacity);
        }
        String m = new String(cache);
        log.info("读到的信息：{}", m);
        System.out.println("==================================================================");

        //查询缓冲中，逗号的位置
        int commaIndex = buf.forEachByte(ByteProcessor.FIND_COMMA);
        log.info("逗号的位置：{}", commaIndex);
        char w = 'W';
        byte wByte = (byte) (w & 0xff);
        int wIndex = buf.forEachByte(new ByteProcessor.IndexOfProcessor(wByte));
        log.info("{}的位置：{}", w, wIndex);

        //自定义索引
        buf.readerIndex(3);
        buf.writerIndex(6);
        int readableSize = buf.readableBytes();
        readerIndex = buf.readerIndex();
        writerIndex = buf.writerIndex();
        log.info("自定义索引后，readerIndex = {} , writerIndex = {}, readableSize = {}", readerIndex, writerIndex, readableSize);

        buf.resetReaderIndex();
        buf.resetWriterIndex();
//        buf.discardReadBytes();
        log.info("resetWriterIndex后：{}",buf);
    }
}

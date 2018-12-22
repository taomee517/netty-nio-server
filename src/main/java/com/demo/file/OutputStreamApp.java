package com.demo.file;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author LuoTao
 * @email taomee517@qq.com
 * @date  2018/12/22
 * @time  15:01
 */
public class OutputStreamApp {
    public static void main(String[] args) throws Exception {
        byte[] msg = "Hello,World!".getBytes();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        for(int i=0;i<msg.length;i++){
            byteBuffer.put(msg[i]);
        }
        FileOutputStream fileOutputStream = new FileOutputStream("src\\main\\resources\\output.txt");
        FileChannel fileChannel = fileOutputStream.getChannel();
        byteBuffer.flip();
        fileChannel.write(byteBuffer);
        fileOutputStream.close();
    }
}

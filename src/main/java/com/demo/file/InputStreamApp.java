package com.demo.file;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author LuoTao
 * @email taomee517@qq.com
 * @date 2018/12/22
 * @time 14:49
 */
public class InputStreamApp {
    public static void main(String[] args) throws Exception {
        FileInputStream fileInputStream = new FileInputStream("ReadMe.txt");
        FileChannel channel = fileInputStream.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        channel.read(buffer);
        buffer.flip();
        while (buffer.hasRemaining()){
            byte b = buffer.get();
            System.out.print((char) b);
        }
        fileInputStream.close();

    }
}

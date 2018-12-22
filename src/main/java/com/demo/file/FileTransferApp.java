package com.demo.file;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;

/**
 * @author LuoTao
 * @email taomee517@qq.com
 * @date  2018/12/22
 * @time  16:36
 */
public class FileTransferApp {
    public static void main(String[] args) throws Exception{
        FileInputStream fileInputStream = new FileInputStream("src\\main\\resources\\log4j2.properties");
        FileOutputStream fileOutputStream = new FileOutputStream("src\\main\\resources\\output.txt");
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        while (true){
            byteBuffer.clear();
            int index = fileInputStream.getChannel().read(byteBuffer);
            if(-1 == index){
                break;
            }
            byteBuffer.flip();
            while (byteBuffer.hasRemaining()) {
                fileOutputStream.getChannel().write(byteBuffer);
            }
        }
    }
}

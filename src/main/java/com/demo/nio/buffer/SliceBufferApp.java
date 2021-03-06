package com.demo.nio.buffer;

import java.nio.ByteBuffer;

/**
 * @author LuoTao
 * @email taomee517@qq.com
 * @date 2018/12/22
 * @time 17:26
 */
public class SliceBufferApp {
    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        for(int i=0;i<byteBuffer.capacity();i++){
            byteBuffer.put((byte) i);
        }
        byteBuffer.flip();
//        byte[] bytes = new byte[10];
//        byteBuffer.get(bytes,0,byteBuffer.capacity());
//        String str = new String(bytes);
//        System.out.println(str);
        System.out.println("==========slice操作前==========");
        while (byteBuffer.hasRemaining()){
            System.out.println(byteBuffer.get());
        }
        byteBuffer.position(2).limit(6);
        ByteBuffer sliceByteBuffer = byteBuffer.slice();
        for (int i=0;i<sliceByteBuffer.capacity();i++ ){
            byte b = sliceByteBuffer.get(i);
            b *= 2;
            sliceByteBuffer.put(b);
    }
        byteBuffer.position(0).limit(byteBuffer.capacity());
        System.out.println("==========slice操作后==========");
        while (byteBuffer.hasRemaining()){
            System.out.println(byteBuffer.get());
        }
    }
}

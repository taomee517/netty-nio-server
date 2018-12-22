package com.demo.nio;

import java.nio.IntBuffer;
import java.security.SecureRandom;

/**
 * @author LuoTao
 * @email taomee517@qq.com
 * @date  2018/12/22
 * @time  14:25
 */
public class BufferApp {
    /**
     * java.io  核心组件：流stream(InputStream/OutputStream)
     * java.nio 核心组件：Selector(选择器)/Channel(通道)/Buffer(缓冲区)
     * byte,char,short,int,float,double,String 这7类基本数据类型都有对应的buffer类型；
     * Channel可以写入和读出操作，类型Stream，但Channel是双向的，Stream是单向的；
     */
    public static void main(String[] args) {
        IntBuffer buffer = IntBuffer.allocate(10);
        for (int i=0;i<buffer.capacity();i++){
            int num = new SecureRandom().nextInt(100);
            buffer.put(num);
        }
        buffer.flip();
        int index = 0;
        while (buffer.hasRemaining()){
            index ++;
            System.out.println("第"+ index +"位：" + buffer.get());
        }
    }
}


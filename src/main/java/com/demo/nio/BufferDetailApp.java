package com.demo.nio;

import java.nio.IntBuffer;
import java.security.SecureRandom;

/**
 * @author LuoTao
 * @email taomee517@qq.com
 * @date 2018/12/22
 * @time 15:17
 */
public class BufferDetailApp {
    /**
     * 通过一个小demo，深入理解Buffer的三个重要属性：position,limit,capacity
     * position: 下一个将要被写入或者读取的元素索引,新创建一个Buffer对象时，position被初始化为0
     * limit: 指Buffer中还有多少数据需要取出（官方： A buffer's limit is the index of the first element that should
     *  not be read or written.第一个不能被读或者写的元素的索引）
     * capacity：指定了可以存储在缓冲区中的最大数据容量，xxBuffer.allocate()时指定
     * mark: 初始化时mark为-1
     * 0 <= mark <= position <= limit <= capacity
     * 
     * 总结：flip是为从buffer读取信息为准备，clear是为写内容到buffer做准备
     */
    public static void main(String[] args) {
        IntBuffer buffer = IntBuffer.allocate(10);       
        System.out.println("========Buffer初始化时======");
        System.out.println("capacity:"+ buffer.capacity());
        System.out.println("limit:"+ buffer.limit());
        System.out.println("position:"+ buffer.position());
        for(int i=0;i<5;i++){
            int num = new SecureRandom().nextInt(10);
            buffer.put(num);
            System.out.println("========写入第"+ (i+1) +"个元素进Buffer时======");
            System.out.println("capacity:"+ buffer.capacity());
            System.out.println("limit:"+ buffer.limit());
            System.out.println("position:"+ buffer.position());
        }
        System.out.println("========Buffer flip前======");
        System.out.println("capacity:"+ buffer.capacity());
        System.out.println("limit:"+ buffer.limit());
        System.out.println("position:"+ buffer.position());
        buffer.flip();
        System.out.println("========Buffer flip后======");
        System.out.println("capacity:"+ buffer.capacity());
        System.out.println("limit:"+ buffer.limit());
        System.out.println("position:"+ buffer.position());
        int index = 0;
        while (buffer.hasRemaining()){
            index ++;
            System.out.println("第"+ index +"位随机数：" + buffer.get());
            System.out.println("========从Buffer读出第"+ index +"个元素时======");
            System.out.println("capacity:"+ buffer.capacity());
            System.out.println("limit:"+ buffer.limit());
            System.out.println("position:"+ buffer.position());
        }

        System.out.println("========Buffer clear前======");
        System.out.println("capacity:"+ buffer.capacity());
        System.out.println("limit:"+ buffer.limit());
        System.out.println("position:"+ buffer.position());
        buffer.clear();
        System.out.println("========Buffer clear后======");
        System.out.println("capacity:"+ buffer.capacity());
        System.out.println("limit:"+ buffer.limit());
        System.out.println("position:"+ buffer.position());
    }
}

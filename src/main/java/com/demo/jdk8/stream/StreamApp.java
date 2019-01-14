package com.demo.jdk8.stream;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author LuoTao
 * @email taomee517@qq.com
 * @date 2018/12/26
 * @time 10:31
 */
public class StreamApp {
    public static void main(String[] args) {
        List<Integer> array = new ArrayList<>();
        for(int i=0;i<100;i++){
            array.add(i);
        }
        /** stream类似一个增强的Iterator,遍历出来应该是无序的*/
        Stream<Integer> stream = array.stream().filter(p->p>90);
        System.out.println("Stream过滤后,大于90的数为:");
        stream.forEach(p-> System.out.print(p + "\t"));
        System.out.println();

        Stream<Integer> parallelStream = array.parallelStream().filter(p->p>90);
        System.out.println("ParalleStream过滤后,大于90的数为:");
        parallelStream.forEach(p-> System.out.print(p + "\t"));
    }
}

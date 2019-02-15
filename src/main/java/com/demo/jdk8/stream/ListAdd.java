package com.demo.jdk8.stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author LuoTao
 * @email taomee517@qq.com
 * @date 2019/1/24
 * @time 17:29
 */
public class ListAdd {
    private static List<Object> list = new ArrayList<>();
    private static int maxBatchSize = 10;
    public static void main(String[] args) {
        List<Object> fv = new ArrayList<>();
        List<Object> inputList;
        List<Integer> a = Arrays.asList(1,2,3);
        List<Integer> b = Collections.singletonList(4);
        List<Integer> c = Collections.singletonList(5);
        List<Integer> d = Arrays.asList(6,7,8,9);
        List<String>  x = Arrays.asList("a","b","c");
        List<String>  y = Collections.singletonList("d");
        inputList = Arrays.asList(a,x,b,c,y,d);
        for(Object o : inputList){
            List<Object> sl = (List)o;
            for(Object obj:sl){
                fv = putAndGetList(obj);
                if(fv != null){
                    break;
                }
            }
        }
        for(Object o : fv){
            System.out.println(o.toString());
        }
    }

    public static synchronized List<Object> putAndGetList(Object t) {
        synchronized (list) {
            if (list.size() >= maxBatchSize) {
                List<Object> newList = new ArrayList<>();
                list.add(t);
                newList.addAll(list);
                list.clear();
                return newList;
            } else {
                list.add(t);
                return null;
            }
        }
    }
}

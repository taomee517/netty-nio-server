package com.demo.jdk8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author LuoTao
 * @email taomee517@qq.com
 * @date 2019/4/8
 * @time 14:05
 */
public class ListShow {
    public static void main(String[] args) {
        List<String> errList = new ArrayList<>();
        errList.add("9834098045984380");
        errList.add("8398742979879323");
        System.out.println(Arrays.toString(errList.toArray()));
    }
}

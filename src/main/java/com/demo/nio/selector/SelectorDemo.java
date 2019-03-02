package com.demo.nio.selector;

import java.nio.channels.Selector;

/**
 * @author LuoTao
 * @email taomee517@qq.com
 * @date 2019/2/21
 * @time 10:22
 */
public class SelectorDemo {
    public static final int MAX_SIZE = 5;
    public static void main(String[] args) throws Exception {
        for(int i=0;i<MAX_SIZE;i++){
            Selector.open();
            Thread.sleep(100000);
        }
    }
}

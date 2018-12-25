package com.demo.netty;

import com.demo.pojo.Worker;


import java.util.Arrays;
import java.util.List;
/**
 * @author LuoTao
 * @email taomee517@qq.com
 * @date  2018/12/22
 * @time  11:14
 */
public class NettyServer{

    public static void main(String[] args){
        try {
            String text = "WEARETHECHAMPIONS";
            List<String> arr = Arrays.asList("we","are","the","champions");
            for (String word:arr
            ) {
                System.out.print(word);
                System.out.printf(" ");
            }
            System.out.println("!");
            System.out.println("终于设置成功了！");
            Worker worker = new Worker(1001,"研发部","李月舒","测试");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getAgeByName() {
        return null;
    }
}

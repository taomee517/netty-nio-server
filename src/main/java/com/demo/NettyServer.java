package com.demo;

import com.demo.pojo.Worker;
import com.sun.deploy.util.StringUtils;


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
            StringUtils.splitString(text,",");
            List<String> arr = Arrays.asList("we","are","the","champions");
            for (String word:arr
            ) {
                System.out.print(word);
                System.out.printf(" ");
            }
            System.out.println("!");
            System.out.println("终于设置成功了！");
            StringUtils.splitString(text,"ARE");
            Worker worker = new Worker(1001,"研发部","李月舒","测试");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getAgeByName() {
        return null;
    }
}

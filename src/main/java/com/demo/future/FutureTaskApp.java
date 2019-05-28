package com.demo.future;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @author LuoTao
 * @email taomee517@qq.com
 * @date  2018/12/26
 * @time  8:47
 */
@Slf4j
public class FutureTaskApp {
    public static void main(String[] args) throws Exception {
        long startTime = System.currentTimeMillis();
        //网购厨具
        Callable<Tool> callable = new Callable<Tool>() {
            @Override
            public Tool call() throws Exception {
                log.info("网购厨具:下单");
                log.info("网购厨具:付款");
                Thread.sleep(3000);
                log.info("====================");
                log.info("网购厨具:收到厨具");
                return new Tool();
            }
        };
        //到超市购买食材
        FutureTask<Tool> task = new FutureTask<Tool>(callable);
        new Thread(task).start();
        Food food = new Food();
        log.info("准备食材:已到位!");

        if(!task.isDone()){
            log.info("网购厨具:还没收到厨具,请联系快递95886");
        }
        Tool tool = task.get();
        cook(food,tool);
        long endTime = System.currentTimeMillis();
        log.info("全部就位,开始做饭!总耗时:"+ (endTime-startTime) + "ms");


    }

    static void cook(Food food,Tool tool){};
    static class Food{};
    static class Tool{};

}

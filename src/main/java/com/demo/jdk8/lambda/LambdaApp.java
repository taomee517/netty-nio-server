package com.demo.jdk8.lambda;

import java.util.Arrays;
import java.util.List;
/**
 * @author LuoTao
 * @email taomee517@qq.com
 * @date  2018/12/22
 * @time  11:14
 */
public class LambdaApp {


    public static void main(String... args) throws Exception {

        String[] players = {"Rafael Nadal", "Novak Djokovic",
                "Stanislas Wawrinka", "David Ferrer",
                "Roger Federer", "Andy Murray",
                "Tomas Berdych", "Juan Martin Del Potro",
                "Richard Gasquet", "John Isner"};

        Arrays.sort(players,(String s1,String s2)->(s1.compareTo(s2)));
        List<String> team = Arrays.asList(players);
        team.forEach(player->System.out.println(player));
    }
}


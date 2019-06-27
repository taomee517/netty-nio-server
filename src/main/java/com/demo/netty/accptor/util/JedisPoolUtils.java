package com.demo.netty.accptor.util;

import com.blackTea.common.datasource.KvDao;
import kv.m.KV_Client;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author LuoTao
 * @email taomee517@qq.com
 * @date 2019/2/16
 * @time 8:15
 */
public class JedisPoolUtils {

    public static Jedis getJedis(){
        return KvDao.use().getJedis();
    }

    public static void main(String[] args) {
        Jedis jedis = JedisPoolUtils.getJedis();
        if("PONG".equals(jedis.ping().toUpperCase())){
            System.out.println("连接成功！");
        }
    }
}

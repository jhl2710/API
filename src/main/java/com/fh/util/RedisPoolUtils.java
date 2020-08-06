package com.fh.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

//redis的连接池
public class RedisPoolUtils {

    //初始化连接池
    private static JedisPool jedisPool;

    private RedisPoolUtils(){

    }

    static {
        JedisPoolConfig jedisPoolConfig=new JedisPoolConfig();
        jedisPoolConfig.setMinIdle(25);
        jedisPoolConfig.setMaxIdle(50);
        jedisPoolConfig.setMaxTotal(100);
        jedisPoolConfig.setMaxWaitMillis(30000);
        jedisPool=new JedisPool(jedisPoolConfig,"192.168.178.129",6379);
    }

    //从池中拿连接
    public static Jedis getJedis(){
        return jedisPool.getResource();
    }

    //连接用还给池中
    public static void returnJedis(Jedis jedis){
        jedisPool.returnResource(jedis);
    }


}

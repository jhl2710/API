package com.fh.util;

import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;


public class RedisUse {

    public static void set(String key,String value){
        Jedis jedis = RedisPoolUtils.getJedis();
        jedis.set(key,value);
        RedisPoolUtils.returnJedis(jedis);
    }

    public static void set(String key,String value,int seconds){
        Jedis jedis = RedisPoolUtils.getJedis();
        jedis.setex(key,seconds,value);
        RedisPoolUtils.returnJedis(jedis);
    }

    public static String get(String key){
        Jedis jedis = RedisPoolUtils.getJedis();
        String value=jedis.get(key);
        RedisPoolUtils.returnJedis(jedis);
        return value;
    }

    public static void hset(String key,String filed,String value){
        Jedis jedis = RedisPoolUtils.getJedis();
        Long hset = jedis.hset(key, filed, value);
        RedisPoolUtils.returnJedis(jedis);
    }

    public static long hlen(String key){
        Jedis jedis = RedisPoolUtils.getJedis();
        Long hlen = jedis.hlen(key);
        RedisPoolUtils.returnJedis(jedis);
        return hlen;
    }

    public static Map<String, String> hgetAll(String key){
        Jedis jedis = RedisPoolUtils.getJedis();
        Map<String, String> map = jedis.hgetAll(key);
        RedisPoolUtils.returnJedis(jedis);
        return map;
    }

    public static String hget(String key,String filed){
        Jedis jedis = RedisPoolUtils.getJedis();
        String hget = jedis.hget(key, filed);
        RedisPoolUtils.returnJedis(jedis);
        return hget;
    }

    public static List<String> hvals(String key){
        Jedis jedis = RedisPoolUtils.getJedis();
        List<String> hvals = jedis.hvals(key);
        RedisPoolUtils.returnJedis(jedis);
        return hvals;
    }

    public static String getAreaName(String areaId) {
        StringBuffer sb=new StringBuffer();//中文名
        String[] split = areaId.split(",");
        for (int i = 0; i < split.length; i++) {
            String id=split[i];
            String areaStr = RedisUse.hget("areaList_h",id);
            sb.append(areaStr).append(",");
        }
        return sb.toString();
    }


    public static void hdel(String key,String filed){
        Jedis jedis = RedisPoolUtils.getJedis();
        jedis.hdel(key,filed);
        RedisPoolUtils.returnJedis(jedis);
    }


    public static boolean exists(String key){
        Jedis jedis = RedisPoolUtils.getJedis();
        Boolean exists = jedis.exists(key);
        RedisPoolUtils.returnJedis(jedis);
        return exists;
    }

}

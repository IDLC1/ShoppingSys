package com.taotao.rest.dao.impl;

import com.taotao.rest.dao.JedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisCluster;

public class JedisClientCluster implements JedisClient {

    @Autowired
    private JedisCluster jedisCluster;

    @Override
    public String get(String key) {
        String str = jedisCluster.get(key);
        return str;
    }

    @Override
    public String set(String key, String value) {
        return jedisCluster.set(key, value);
    }

    @Override
    public String hget(String hkey, String key) {
        return jedisCluster.hget(hkey, key);
    }

    @Override
    public Long hset(String hkey, String key, String value) {
        Long res = jedisCluster.hset(hkey, key, value);
        return res;
    }

    @Override
    public long incr(String key) {
        return jedisCluster.incr(key);
    }

    @Override
    public long expire(String key, int second) {
        return jedisCluster.expire(key, second);
    }

    @Override
    public long ttl(String key) {
        return jedisCluster.ttl(key);
    }
}

package com.taotao;

import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;

public class jedisTest {

    @Test
    public void testJedisSingle() {
        // 创建一个jedis对象
        Jedis jedis = new Jedis("www.greattom.xyz", 6366);
        // 调用jedis对象的方法，方法名称和redis的命令一致
        jedis.set("key1", "jedis test");
        String str = jedis.get("key1");
        System.out.println(str);
        // 关闭jedis
        jedis.close();
    }

    /**
     * 使用连接池
     */
    @Test
    public void testJedisPool() {
        // 创建jedis连接池
        JedisPool pool = new JedisPool("www.greattom.xyz", 6366);
        // 从连接池湖区Jedis对象
        Jedis jedis = pool.getResource();
        // 调用jedis对象的方法，方法名称和redis的命令一致
        jedis.set("key1", "jedis test");
        String str = jedis.get("key1");
        System.out.println(str);
        // 关闭jedis
        jedis.close();
        pool.close();
    }

    /**
     * 连接集群
     */
    @Test
    public void testJedisCluster() {
        HashSet<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort("www.greattom.xyz", 7001));
        nodes.add(new HostAndPort("www.greattom.xyz", 7002));
        nodes.add(new HostAndPort("www.greattom.xyz", 7003));
        nodes.add(new HostAndPort("www.greattom.xyz", 7004));
        nodes.add(new HostAndPort("www.greattom.xyz", 7005));
        nodes.add(new HostAndPort("www.greattom.xyz", 7006));
        // 创建jedis连接池
        JedisCluster cluster = new JedisCluster(nodes);
        // 调用jedis对象的方法，方法名称和redis的命令一致
        cluster.set("key1", "jedis test cluster!");
        String str = cluster.get("key1");
        System.out.println(str);
        // 关闭jedis
        cluster.close();
    }
}

package com.taotao;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;

public class jedisTest {

    public static final Logger Log = LoggerFactory.getLogger(jedisTest.class);

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

    @Test
    public void testSpringJedisSingle() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
        JedisPool jedisPool = (JedisPool) applicationContext.getBean("redisClient");
        Jedis jedis = jedisPool.getResource();
        jedis.set("key22", "jedis testSpringJedisSingle");
        String str = jedis.get("key22");
        Log.info(str);
        jedis.close();
        jedisPool.close();
    }

    @Test
    public void testSpringJedisCluster() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
        JedisCluster jedisCluster = (JedisCluster) applicationContext.getBean("redisClusterClient");
        jedisCluster.set("keyclu", "jedis testSpringJedisCluster");
        String str = jedisCluster.get("keyclu");
        Log.info(str);
        jedisCluster.close();
    }
}

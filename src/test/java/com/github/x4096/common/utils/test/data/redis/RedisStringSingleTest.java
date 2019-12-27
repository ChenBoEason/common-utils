package com.github.x4096.common.utils.test.data.redis;

import com.github.x4096.common.utils.data.redis.interf.impl.StringJedisTemplate;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: common-utils
 * @DateTime: 2019-10-09 12:29
 * @Description:
 */
public class RedisStringSingleTest {

    private static CountDownLatch startSignal = new CountDownLatch(1);
    //用来表示裁判员需要维护的是6个运动员
    private static CountDownLatch endSignal = new CountDownLatch(6);

    public static void main(String[] args) throws InterruptedException {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        /* 单节点 */
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName("192.168.2.103");
        redisStandaloneConfiguration.setPort(6379);
        redisStandaloneConfiguration.setDatabase(0);


        /* 单节点连接池 */
        JedisClientConfiguration jedisClientConfiguration = JedisClientConfiguration.builder()
                .connectTimeout(Duration.ofMillis(1_000))
                .usePooling().poolConfig(new GenericObjectPoolConfig())
                .build();
        // JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration);


        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration);


        stringRedisTemplate.setConnectionFactory(jedisConnectionFactory);
        stringRedisTemplate.afterPropertiesSet();


        StringJedisTemplate stringJedisTemplate = new StringJedisTemplate(stringRedisTemplate);

        String value = "1";
        stringJedisTemplate.set("hhh", value);

        System.err.println(stringJedisTemplate.get("hhh"));
    }

}

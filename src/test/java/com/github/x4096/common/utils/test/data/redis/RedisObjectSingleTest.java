package com.github.x4096.common.utils.test.data.redis;

import com.github.x4096.common.utils.data.redis.interf.impl.ObjectJedisTemplate;
import com.github.x4096.common.utils.test.pojo.Student;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: common-utils
 * @DateTime: 2019-10-09 12:54
 * @Description:
 */
public class RedisObjectSingleTest {

    public static void main(String[] args) {

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        /* 设置key序列化方式 默认使用 jdk 的序列化,导致存入Redis的key变成 \xAC\xED\x00\x05t\x00\x03key */
        RedisSerializer redisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(redisSerializer);
        redisTemplate.setHashKeySerializer(redisSerializer);


        /* 单节点 */
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName("127.0.0.1");
        redisStandaloneConfiguration.setPort(6379);
        redisStandaloneConfiguration.setDatabase(0);
        redisStandaloneConfiguration.setPassword("");



        /* 单节点连接池 */
        JedisClientConfiguration jedisClientConfiguration = JedisClientConfiguration.builder()
                .connectTimeout(Duration.ofMillis(1_000))
                .usePooling().poolConfig(new GenericObjectPoolConfig())
                .build();
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration);
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        redisTemplate.afterPropertiesSet();



        ObjectJedisTemplate objectJedisTemplate = new ObjectJedisTemplate(redisTemplate);

        Student student = new Student();
        student.setAge(18);
        student.setUsername("我梦");

        objectJedisTemplate.set("key", student);

        Student student2 = (Student) objectJedisTemplate.get("key");

        System.err.println(student2);
    }
}

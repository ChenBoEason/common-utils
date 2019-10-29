package com.github.x4096.common.utils.test.data.redis;

import com.github.x4096.common.utils.data.redis.interf.impl.StringJedisTemplate;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;
import java.util.Map;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: common-utils
 * @DateTime: 2019-10-09 12:29
 * @Description:
 */
public class RedisStringSingleTest {

    public static void main(String[] args) {
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

        stringJedisTemplate.set("key", "value");
        Boolean res = stringJedisTemplate.delete("key");

        System.err.println(res);


        // for (int i = 0; i < 100; i++) {
        //     stringJedisTemplate.hPut("alipay", RandomStringUtils.number(5), DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        // }

        Map<Object, Object> hash = stringJedisTemplate.hGetAll("alipay");

        hash.forEach((key, value) -> {
            System.err.println(key + ": " + value);
            String k = (String) key;
            int kInt = Integer.valueOf(k);
            if (kInt % 2 == 0) {
                stringJedisTemplate.hDelete("alipay", key);
            }
        });


    }

}

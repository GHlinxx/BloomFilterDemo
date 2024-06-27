package com.example.sparrow.redis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @Description:
 * @Author: 林澈
 * @Created: 2024/6/26 21:27
 */
@Component
public class MyJedisPoolConfig {

    @Value("${spring.redis.host}")
    private String host;
    @Bean
    public JedisPool getJedisPool(){
        return new JedisPool(new JedisPoolConfig(), host, 6379);
    }
}

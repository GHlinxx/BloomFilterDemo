package com.example.sparrow.redis.service.impl;

import com.example.sparrow.redis.service.RedisLuaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Description: redisService 实现类
 * @Author: 林澈
 * @Created: 2024/6/25 0:16
 */
@Slf4j
@Service
public class RedisLuaServiceImpl implements RedisLuaService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private Integer SIZE = 1000000;
    @Override
    public Object addsLuaBloomFilter(String filterName, List<String> values) {
        Boolean isKey = stringRedisTemplate.hasKey(filterName);
        log.info("redis.key=" + filterName + " is " + isKey);
        if (!isKey) {
//            设置容量为100万，错误率为1%
            createLuaBloomFilter(filterName, SIZE, 0.01);
            setKeyExpiration(filterName, 7, TimeUnit.MINUTES); // 设置过期时间为7天
        }
        DefaultRedisScript<Boolean> bloomAdd = new DefaultRedisScript<>();
        bloomAdd.setScriptSource(new ResourceScriptSource(new ClassPathResource("bloomFilter-inster.lua")));
        bloomAdd.setResultType(Boolean.class);
        /**
         * 这里调用方法 execute(RedisScript<T> script, List<K> keys, Object... args)
         * 这里的keys 对于 lua脚本中的 KEY[i]  这个i跟集合大小有关
         * 这里的args 对于 lua脚本中的 ARGV[i] 这个i跟加入可变参数的个数有关
         */
        List<String> keyList = new ArrayList<>();
        keyList.add(filterName);
        keyList.add(values.get(0));
        Object execute = stringRedisTemplate.execute(bloomAdd, values, filterName);
        return execute;
    }

    // 创建新的布隆过滤器
    private void createLuaBloomFilter(String filterName, long capacity, double errorRate) {
        String script = "return redis.call('BF.RESERVE', KEYS[1], ARGV[1], ARGV[2])";
        List<String> keys = Collections.singletonList(filterName);
        List<String> args = new ArrayList<>();
        args.add(String.valueOf(errorRate));
        args.add(String.valueOf(capacity));
        stringRedisTemplate.execute(new DefaultRedisScript<>(script, Boolean.class), keys, args.toArray());
    }

    // 设置键的过期时间
    public void setKeyExpiration(String key, long timeout, TimeUnit unit) {
        stringRedisTemplate.expire(key, timeout, unit);
    }

    @Override
    public Boolean existsLuabloomFilter(String filterName, String value) {
        DefaultRedisScript<Boolean> bloomExists= new DefaultRedisScript<>();
        bloomExists.setScriptSource(new ResourceScriptSource(new ClassPathResource("bloomFilter-exist.lua")));
        bloomExists.setResultType(Boolean.class);
        List<String> keyList= new ArrayList<>();
        keyList.add(filterName);
        keyList.add(value);
        Boolean result = stringRedisTemplate.execute(bloomExists,keyList);
        return result;
    }

}

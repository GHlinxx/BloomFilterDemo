package com.example.sparrow.redis.service.impl;

import com.example.sparrow.redis.service.RedisPipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.commands.ProtocolCommand;

import java.util.List;

/**
 * @Description:
 * @Author: 林澈
 * @Created: 2024/6/25 9:19
 */
@Service
public class RedisPipServiceImpl implements RedisPipService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private JedisPool jedisPool;

    @Override
    public Object addsPipBloomFilter(String filterName, List<String> values) {
        int batchSize = 1000;
//        for (int i = 0; i < values.size(); i += batchSize) {
//            int end = Math.min(i + batchSize, values.size());
//            List<String> batch = values.subList(i, end);
//
//            redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
//                for (String value : batch) {
//                    connection.execute("BF.ADD", filterName.getBytes(), value.getBytes());
//                }
//                return null;
//            });
//        }
        try (Jedis jedis = jedisPool.getResource()) {
            Pipeline pipeline = jedis.pipelined();
            for (String value : values) {
                jedis.sendCommand(new ProtocolCommand() {
                    @Override
                    public byte[] getRaw() {
                        return "BF.ADD".getBytes();
                    }
                }, filterName, value);
            }
            pipeline.sync();
        }
        return null;
    }

    // 获取布隆过滤器信息
    public List<Object> getBloomFilterInfo(String filterName) {
        try (Jedis jedis = jedisPool.getResource()) {
            return (List<Object>) jedis.sendCommand(new ProtocolCommand() {
                @Override
                public byte[] getRaw() {
                    return "BF.INFO".getBytes();
                }
            }, filterName);
        }
    }

    @Override
    public Boolean existsPipBoolmFilter(String filterName, String value) {
        return redisTemplate.execute((RedisCallback<Boolean>) connection -> connection.sIsMember(filterName.getBytes(), value.getBytes()));
    }
}

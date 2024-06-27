package com.example.sparrow.redis.service;

import java.util.List;

/**
 * @Description:
 * @Author: 林澈
 * @Created: 2024/6/25 9:19
 */
public interface RedisPipService {

    Object addsPipBloomFilter(String filterName, List<String> values);

    Boolean existsPipBoolmFilter(String filterName, String value);

    public List<Object> getBloomFilterInfo(String filterName);
}

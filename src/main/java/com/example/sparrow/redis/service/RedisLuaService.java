package com.example.sparrow.redis.service;

import java.util.List;

/**
 * @Description: redis --》插入数据，判断数据
 * @Author: 林澈
 * @Created: 2024/6/25 0:14
 */
public interface RedisLuaService {

    /**
     * 布隆过滤器 批量插入数据 通过LUA
     *
     * @param filterName 布隆过滤器名称
     * @param values 插入布隆过滤器的值
     */
    Object addsLuaBloomFilter(String filterName, List<String> values);

    /**
     *
     * @param filterName 布隆过滤器名称
     * @param value      查询该key是否存在
     * @return
     */
    Boolean existsLuabloomFilter(String filterName, String value);

}

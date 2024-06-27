package com.example.sparrow.redis.web;

import com.example.sparrow.redis.service.RedisLuaService;
import com.example.sparrow.redis.service.RedisPipService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description:
 * @Author: 林澈
 * @Created: 2024/6/25 0:21
 */
@Slf4j
@RestController
public class RedisBloomFilterController {
    @Autowired
    private RedisLuaService redisLuaService;

    @Autowired
    private RedisPipService redisPipService;

    public static final String BLOOM_KEY = "bloom_key";

    @RequestMapping("/addBloomFilter")
    public Object addBloomFilter(String email) {
        Object bloomFilter = redisLuaService.addsLuaBloomFilter(BLOOM_KEY, Arrays.asList(email.split(";")));
        log.info("保存 结束===>{}A", bloomFilter);
        return bloomFilter;
    }

    @RequestMapping("/addBloomFilterByFile")
    public Object addBloomFilterByFile() {
        Resource resource = new ClassPathResource("blacklist.txt");
        List<String> emailList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(resource.getFile().getPath()))) {
            String email ;
            while ((email = reader.readLine()) != null) {
                emailList.add(email);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        Object bloomFilter = redisLuaService.addsLuaBloomFilter(BLOOM_KEY, emailList);
        log.info("保存 结束===>{}A", bloomFilter);
        return bloomFilter;
    }

    @RequestMapping("/addBloomPipFilterByFile")
    public Object addBloomPipFilterByFile() {
        Resource resource = new ClassPathResource("blacklist.txt");
        List<String> emailList = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        try (BufferedReader reader = new BufferedReader(new FileReader(resource.getFile().getPath()))) {
            String email ;
            while ((email = reader.readLine()) != null) {
                emailList.add(email);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        redisPipService.addsPipBloomFilter(BLOOM_KEY, emailList);
        log.info("bloom 管道PIP插入10w条数据成功，耗时：{}", (System.currentTimeMillis() - startTime) / 1000);
        return null;
    }

    @RequestMapping("/findBloomInfo")
    public Object findBloomInfo() throws UnsupportedEncodingException {
        List<Object> bloomFilterInfo = redisPipService.getBloomFilterInfo(BLOOM_KEY);
        System.out.println(BLOOM_KEY + "具体信息：" );
        for (int i = 0; i < bloomFilterInfo.size(); i++) {
            Object b = bloomFilterInfo.get(i);
            if (b instanceof byte[]) {
                String key = new String((byte[]) bloomFilterInfo.get(i), "UTF-8");
                System.out.print("key=" + key);
            } else {
                System.out.print(",value=" + b + "\n");
            }
        }
        return bloomFilterInfo;
    }

    @RequestMapping("/isContains")
    public Boolean isContains(String email){
        if (!redisLuaService.existsLuabloomFilter(BLOOM_KEY, email)) {
            log.info("redis布隆过滤器不存在该数据=============数据{}", email);
            return false;
        } else {
            log.info("redis布隆过滤器存在该数据=============数据{}", email);
            return true;
        }
    }

}

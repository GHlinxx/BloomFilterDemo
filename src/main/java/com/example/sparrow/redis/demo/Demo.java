package com.example.sparrow.redis.demo;

import com.example.sparrow.redis.config.MyBloomFilter;

/**
 * @Description:
 * @Author: 林澈
 * @Created: 2024/6/24 21:26
 */
public class Demo {
    public static void main(String[] args) {
        String h1 = "https://www.baidu.com";
        String h2 = "https://www.csdn.com";
        String h3 = "https://www.juejin.com";
        MyBloomFilter filter = new MyBloomFilter();
        System.out.println(filter.contains(h1));
        System.out.println(filter.contains(h2));
        System.out.println(filter.contains(h3));
        filter.add(h1);
        filter.add(h2);
        filter.add(h3);

        System.out.println(filter.contains(h1));
        System.out.println(filter.contains(h2));
        System.out.println(filter.contains(h3));
    }
}

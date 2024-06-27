package com.example.sparrow.redis.config;

import org.springframework.stereotype.Component;

import java.util.BitSet;

/**
 * @Description: 自定义的布隆过滤器
 * @Author: 林澈
 * @Created: 2024/6/21 15:04
 */
//@Component
public class MyBloomFilter {
    // 二进制向量（数组）的位数，
    // 2算术左移29 结果为 1073741824  10亿7千万bit
    public static final int BIT_CAP = 2 << 29;
    // 初始化BitSet容器
    private final BitSet bits = new BitSet(BIT_CAP);
    /**
     * 通过这个数组可以创建 6 个不同的哈希函数
     */
    private static final int[] SEEDS = new int[]{3, 13, 46, 71, 91, 134};
    // 初始化Hash对象
    private final SimpleHash[] func = new SimpleHash[SEEDS.length];

    public MyBloomFilter(){
        for (int i = 0; i < SEEDS.length; i++) {
            func[i] = new SimpleHash(BIT_CAP, SEEDS[i]);
        }
    }

    public void add(Object o){
        for (SimpleHash hash : func) {
            bits.set(hash.hashCode(o), true);
        }
    }

    public Boolean contains(Object value){
        boolean ret = true;
        for (SimpleHash hash : func) {
            ret = ret && bits.get(hash.hashCode(value));
        }
        return ret;
    }

    class SimpleHash{
        private int cap;
        private int seed;

        public SimpleHash(){}

        public SimpleHash(int cap,int seed){
            this.cap = cap;
            this.seed = seed;
        }

        public int hashCode(Object value) {
            int h;
            return (value == null) ? 0 : Math.abs(seed * (cap - 1) & ((h = value.hashCode()) ^ (h >>> 16)));
        }
    }

}

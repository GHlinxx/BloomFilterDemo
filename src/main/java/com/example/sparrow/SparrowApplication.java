package com.example.sparrow;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootApplication
public class SparrowApplication {

    public static void main(String[] args) {
        SpringApplication.run(SparrowApplication.class, args);
    }
}

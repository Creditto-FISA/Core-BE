package org.creditto.core_banking.domain.redis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
public class RedisTest {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    void testRedisConnection() {
        redisTemplate.opsForValue().set("test-key", "redis");
        String value = redisTemplate.opsForValue().get("test-key");
        System.out.println("Redis Value: " + value);
    }
}

package org.creditto.core_banking.domain.redis;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void testRedisConnection() {
        String key = "test-key";
        String expectedValue = "redis";
        redisTemplate.opsForValue().set(key, expectedValue);
        Object value = redisTemplate.opsForValue().get(key);
        Assertions.assertThat(value).isEqualTo(expectedValue);
        System.out.println("Redis Value: " + value);
    }
}

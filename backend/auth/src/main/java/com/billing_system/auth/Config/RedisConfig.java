package com.billing_system.auth.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@Configuration
public class RedisConfig {
    @Bean
    public StringRedisTemplate redisTemplate(RedisConnectionFactory connectionFactory){
        return new StringRedisTemplate(connectionFactory);
    }
    @Bean
    public ValueOperations<String, String> valueOperations(StringRedisTemplate redisTemplate) {
        return redisTemplate.opsForValue();
    }
}

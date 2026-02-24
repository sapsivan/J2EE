package com.example.paymentapp.config;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

@Component
public class RedisHealthIndicator implements HealthIndicator {

    private final RedisConnectionFactory redisConnectionFactory;

    public RedisHealthIndicator(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }

    @Override
    public Health health() {
        try {
            redisConnectionFactory.getConnection().ping();
            return Health.up().withDetail("redis", "Available").build();
        } catch (Exception e) {
            return Health.down().withDetail("redis", "Not Available").build();
        }
    }
}
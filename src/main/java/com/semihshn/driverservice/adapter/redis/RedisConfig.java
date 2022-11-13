package com.semihshn.driverservice.adapter.redis;

import com.semihshn.driverservice.adapter.redis.contactInfo.ContactInfoCache;
import com.semihshn.driverservice.adapter.redis.driver.DriverCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Slf4j
@Configuration
public class RedisConfig {

    private final RedisProperties redisProperties;

    public RedisConfig(RedisProperties redisProperties) {
        this.redisProperties = redisProperties;
    }

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();

        log.warn("redis host: "+redisProperties.getHost());
        log.warn("redis port: "+redisProperties.getPort());

        redisStandaloneConfiguration.setHostName(redisProperties.getHost());
        redisStandaloneConfiguration.setPort(redisProperties.getPort());

        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean("contactInfoRedisTemplate")
    public RedisTemplate<String, ContactInfoCache> contactInfoRedisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisTemplate<String, ContactInfoCache> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setConnectionFactory(lettuceConnectionFactory);
        return template;
    }

    @Bean("driverRedisTemplate")
    public RedisTemplate<String, DriverCache> driverRedisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisTemplate<String, DriverCache> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setConnectionFactory(lettuceConnectionFactory);
        return template;
    }
}

package com.flab.tableorder.config;

import com.flab.tableorder.dto.MenuCategoryDTO;
import com.flab.tableorder.dto.OrderDTO;

import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@ConfigurationProperties(prefix = "spring.redis")
@Setter
public class RedisConfig {
    private String host;
    private int port;
    private String username;
    private String password;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        config.setUsername(username);
        config.setPassword(RedisPassword.of(password));
        return new LettuceConnectionFactory(config);
    }

    private <T> RedisTemplate<String, T> getDefaultRedisTemplate(Class<T> cls, RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, T> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    }

    @Bean
    public RedisTemplate<String, MenuCategoryDTO> menuRedisTemplate(RedisConnectionFactory connectionFactory) {
        return getDefaultRedisTemplate(MenuCategoryDTO.class, connectionFactory);
    }

    @Bean
    public RedisTemplate<String, OrderDTO> orderRedisTemplate(RedisConnectionFactory connectionFactory) {
        return getDefaultRedisTemplate(OrderDTO.class, connectionFactory);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        return getDefaultRedisTemplate(Object.class, connectionFactory);
    }
}

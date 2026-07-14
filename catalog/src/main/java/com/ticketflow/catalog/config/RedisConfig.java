package com.ticketflow.catalog.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
public class RedisConfig {

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                // 1. Время жизни кэша
                .entryTtl(Duration.ofMinutes(10))

                // 2. Защита от кэширования пустых значений
                .disableCachingNullValues()

                // 3. Красивые префиксы в Redis-клиентах (вместо двойного двоеточия '::' будет аккуратная папка ':')
                .computePrefixWith(cacheName -> cacheName + ":")

                // 4. Сериализация текстовых ключей
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))

                // 5. Автоматическая современная сериализация значений в JSON со встроенным @class и JavaTimeModule
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()));
    }
}

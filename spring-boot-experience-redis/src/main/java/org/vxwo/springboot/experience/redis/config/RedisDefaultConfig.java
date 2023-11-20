package org.vxwo.springboot.experience.redis.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.vxwo.springboot.experience.redis.render.RedisTemplateRender;
import org.vxwo.springboot.experience.redis.serializer.RedisPrefixWrapper;


/**
 * @author vxwo-team
 */

@Configuration
public class RedisDefaultConfig {
    private static final String SEPARATOR = ":";

    private String redisNamespace;

    @Autowired
    private void setRedisNamespace(@Value("${experience.redis.namespace:}") String value) {
        redisNamespace = value.trim();
        if (!redisNamespace.isEmpty() && !redisNamespace.endsWith(SEPARATOR)) {
            redisNamespace += SEPARATOR;
        }
    }

    @Bean
    public RedisPrefixWrapper redisPrefixWrapper() {
        return new RedisPrefixWrapper(redisNamespace);
    }

    @Bean
    public RedisTemplateRender redisTemplateRender(RedisConnectionFactory redisConnectionFactory) {
        return new RedisTemplateRender(redisNamespace, redisConnectionFactory);
    }
}


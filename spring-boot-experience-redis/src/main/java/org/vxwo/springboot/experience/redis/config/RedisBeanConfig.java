package org.vxwo.springboot.experience.redis.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.vxwo.springboot.experience.redis.render.RedisTemplateRender;
import org.vxwo.springboot.experience.redis.serializer.RedisPrefixWrapper;
import lombok.extern.slf4j.Slf4j;


/**
 * @author vxwo-team
 */

@Slf4j
@Configuration
@AutoConfigureAfter(RedisConfig.class)
public class RedisBeanConfig {
    private String redisNamespace;

    @Autowired
    public RedisBeanConfig(RedisConfig value) {
        redisNamespace = value.getNamespace();
        if (!redisNamespace.isEmpty() && !redisNamespace.endsWith(value.getNamespaceStuffix())) {
            redisNamespace += value.getNamespaceStuffix();
        }

        if (log.isInfoEnabled()) {
            log.info("Redis actived, namespace: \"" + redisNamespace + "\"");
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


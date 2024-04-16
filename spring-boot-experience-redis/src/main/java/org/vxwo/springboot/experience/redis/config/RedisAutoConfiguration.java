package org.vxwo.springboot.experience.redis.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.vxwo.springboot.experience.redis.processor.RedisFrequencyProcessor;
import org.vxwo.springboot.experience.redis.processor.RedisLockProcessor;
import org.vxwo.springboot.experience.redis.render.RedisTemplateRender;
import org.vxwo.springboot.experience.redis.serializer.RedisPrefixKeySerializer;
import org.vxwo.springboot.experience.redis.serializer.RedisPrefixWrapper;
import lombok.extern.slf4j.Slf4j;


/**
 * @author vxwo-team
 */

@Slf4j
@EnableConfigurationProperties(RedisConfig.class)
public class RedisAutoConfiguration {
    private String redisNamespace;

    @Autowired
    public RedisAutoConfiguration(RedisConfig value) {
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
    public RedisPrefixKeySerializer redisPrefixKeySerializer() {
        return new RedisPrefixKeySerializer(redisNamespace);
    }

    @Bean
    public RedisTemplateRender redisTemplateRender(
            RedisPrefixKeySerializer redisPrefixKeySerializer,
            RedisConnectionFactory redisConnectionFactory) {
        return new RedisTemplateRender(redisPrefixKeySerializer, redisConnectionFactory);
    }

    @Bean
    public RedisLockProcessor redisLockProcessor(RedisTemplateRender render) {
        return new RedisLockProcessor(render);
    }

    @Bean
    public RedisFrequencyProcessor redisFrequencyProcessor(RedisTemplateRender render) {
        return new RedisFrequencyProcessor(render);
    }
}

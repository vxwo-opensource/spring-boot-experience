package org.vxwo.springboot.experience.redis.render;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.vxwo.springboot.experience.redis.serializer.RedisPrefixKeySerializer;
import org.vxwo.springboot.experience.util.json.ObjectMapperBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author vxwo-team
 */

public class RedisTemplateRender {
    private final RedisConnectionFactory connectionFactory;
    private final RedisPrefixKeySerializer keyPrefixSerializer;

    private final static ObjectMapper OBJECT_MAPPER =
            ObjectMapperBuilder.builder().useDefault().build();
    private final static StringRedisSerializer STRING_SERIALIZER = new StringRedisSerializer();

    public RedisTemplateRender(String keyPrefix, RedisConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
        keyPrefixSerializer = new RedisPrefixKeySerializer(keyPrefix);
    }

    public RedisPrefixKeySerializer getPrefixKeySerializer() {
        return keyPrefixSerializer;
    }

    public void renderStringTemplate(RedisTemplate<String, String> template) {
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(keyPrefixSerializer);
        template.setValueSerializer(STRING_SERIALIZER);
        template.setHashKeySerializer(STRING_SERIALIZER);
        template.setHashValueSerializer(STRING_SERIALIZER);
        template.afterPropertiesSet();
    }

    public <T> void renderGenericTemplate(RedisTemplate<String, T> template, Class<T> valueClass) {
        Jackson2JsonRedisSerializer<T> valueSerializer =
                new Jackson2JsonRedisSerializer<T>(valueClass);
        valueSerializer.setObjectMapper(OBJECT_MAPPER);

        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(keyPrefixSerializer);
        template.setValueSerializer(valueSerializer);
        template.setHashKeySerializer(STRING_SERIALIZER);
        template.setHashValueSerializer(valueSerializer);
        template.afterPropertiesSet();
    }
}

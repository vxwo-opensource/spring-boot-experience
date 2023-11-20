package org.vxwo.springboot.experience.redis.render;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.vxwo.springboot.experience.redis.serializer.RedisPrefixKeySerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

/**
 * @author vxwo-team
 */

public class RedisTemplateRender {
    private final ObjectMapper objectMapper;
    private final RedisConnectionFactory connectionFactory;
    private final RedisPrefixKeySerializer keyPrefixSerializer;
    private final static StringRedisSerializer STRING_SERIALIZER = new StringRedisSerializer();

    public RedisTemplateRender(String keyPrefix, RedisConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
        keyPrefixSerializer = new RedisPrefixKeySerializer(keyPrefix);

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new ParameterNamesModule());

        objectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
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
        valueSerializer.setObjectMapper(objectMapper);

        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(keyPrefixSerializer);
        template.setValueSerializer(valueSerializer);
        template.setHashKeySerializer(STRING_SERIALIZER);
        template.setHashValueSerializer(valueSerializer);
        template.afterPropertiesSet();
    }
}

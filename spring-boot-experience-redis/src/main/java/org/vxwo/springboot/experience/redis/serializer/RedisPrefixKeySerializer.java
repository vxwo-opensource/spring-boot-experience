package org.vxwo.springboot.experience.redis.serializer;

import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author vxwo-team
 */

public class RedisPrefixKeySerializer extends StringRedisSerializer {
    private final RedisPrefixWrapper prefixWrapper;

    public RedisPrefixKeySerializer(String prefix) {
        prefixWrapper = new RedisPrefixWrapper(prefix);
    }

    @Override
    public byte[] serialize(String value) {
        return super.serialize(prefixWrapper.wrap(value));
    }

    @Override
    public String deserialize(byte[] bytes) {
        return prefixWrapper.unwrap(super.deserialize(bytes));
    }

};

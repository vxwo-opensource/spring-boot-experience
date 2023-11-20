package org.vxwo.springboot.experience.redis.serializer;

public class RedisPrefixWrapper {
    private final String prefix;
    private final int prefixLength;

    public RedisPrefixWrapper(String prefix) {
        this.prefix = prefix != null ? prefix : "";
        this.prefixLength = this.prefix.length();
    }

    public String wrap(String value) {
        return prefix + value;
    }

    public String unwrap(String value) {
        if (!value.startsWith(prefix)) {
            return value;
        }

        return value.substring(prefixLength);
    }
}

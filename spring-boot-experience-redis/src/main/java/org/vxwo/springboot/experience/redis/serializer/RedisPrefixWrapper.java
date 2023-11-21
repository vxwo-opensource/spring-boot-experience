package org.vxwo.springboot.experience.redis.serializer;

/**
 * String prefix wrapper for Redis
 *
 * @author vxwo-team
 */

public class RedisPrefixWrapper {
    private final String prefix;
    private final int prefixLength;

    public RedisPrefixWrapper(String prefix) {
        this.prefix = prefix != null ? prefix : "";
        this.prefixLength = this.prefix.length();
    }

    /**
     * Wrap the string, add prefix
     *
     * @param value  the stirng
     * @return  the string starts with prefix
     */
    public String wrap(String value) {
        return prefix + value;
    }

    /**
     * Unwrap the string, remove prefix
     *
     * @param value  the stirng
     * @return  the string remove prefix
     */
    public String unwrap(String value) {
        if (!value.startsWith(prefix)) {
            return value;
        }

        return value.substring(prefixLength);
    }
}

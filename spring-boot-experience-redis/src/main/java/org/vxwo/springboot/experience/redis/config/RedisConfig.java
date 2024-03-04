package org.vxwo.springboot.experience.redis.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Data;

/**
 * @author vxwo-team
 */

@Data
@ConfigurationProperties(prefix = "sbexp.redis")
public class RedisConfig {
    private String namespace;
    private String namespaceStuffix;
}

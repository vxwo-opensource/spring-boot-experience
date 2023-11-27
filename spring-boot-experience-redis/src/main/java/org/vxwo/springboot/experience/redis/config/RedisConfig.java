package org.vxwo.springboot.experience.redis.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Data;

/**
 * @author vxwo-team
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "sbexp.redis")
public class RedisConfig {
    private String namespace;
    private String namespaceStuffix;
}

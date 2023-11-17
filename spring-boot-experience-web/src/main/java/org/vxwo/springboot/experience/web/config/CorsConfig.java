package org.vxwo.springboot.experience.web.config;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.vxwo.springboot.experience.web.ConfigPrefix;

/**
 * @author vxwo-team
 */

@Data
@Configuration
@ConfigurationProperties(prefix = ConfigPrefix.CORS)
public class CorsConfig {
    private String allowOrigins;
    private boolean parseReferer;
}

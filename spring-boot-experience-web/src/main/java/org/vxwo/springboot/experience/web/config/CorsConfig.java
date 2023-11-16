package org.vxwo.springboot.experience.web.config;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.vxwo.springboot.experience.web.ConfigPrefix;

/**
 * @author vxwo-team
 */

@Data
@Configuration
@ConfigurationProperties(prefix = ConfigPrefix.CORS)
@PropertySource("classpath:experience/experience-web.properties")
public class CorsConfig {
    private String allowOrigins;
    private boolean parseReferer;
}

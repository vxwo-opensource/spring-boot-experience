package org.vxwo.springboot.experience.web.config;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author vxwo-team
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "experience.web.cors")
@PropertySource("classpath:experience/experience-web.properties")
public class CorsConfig {
    private String allowOrigin;
    private boolean parseReferer;
}

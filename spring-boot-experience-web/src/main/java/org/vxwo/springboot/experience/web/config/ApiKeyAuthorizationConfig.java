package org.vxwo.springboot.experience.web.config;

import lombok.Data;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.vxwo.springboot.experience.web.ConfigPrefix;

/**
 * @author vxwo-team
 */

@Data
@Configuration
@ConfigurationProperties(prefix = ConfigPrefix.AUTHORIZATION_API_KEY)
public class ApiKeyAuthorizationConfig {
    private List<String> headerKeys;
    private boolean parseBearer;
    private List<String> pathRules;
}

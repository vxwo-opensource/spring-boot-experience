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
@ConfigurationProperties(prefix = ConfigPrefix.AUTHORIZATION_BEARER)
public class BearerAuthorizationConfig {
    private List<String> bearerKeys;
    private List<String> pathRules;
}

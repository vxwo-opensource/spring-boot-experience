package org.vxwo.springboot.experience.web.config;

import lombok.Data;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.vxwo.springboot.experience.web.ConfigPrefix;

/**
 * @author vxwo-team
 */

@Data
@ConfigurationProperties(prefix = ConfigPrefix.AUTHORIZATION_BEARER)
public class BearerAuthorizationProperties {
    private List<String> bearerKeys;
    private List<GroupPathRule> pathRules;
}

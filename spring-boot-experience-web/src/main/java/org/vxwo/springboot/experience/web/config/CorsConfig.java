package org.vxwo.springboot.experience.web.config;

import lombok.Data;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.vxwo.springboot.experience.web.ConfigPrefix;

/**
 * @author vxwo-team
 */

@Data
@ConfigurationProperties(prefix = ConfigPrefix.CORS)
public class CorsConfig {
    private boolean parseReferer;
    private List<String> allowOrigins;
}

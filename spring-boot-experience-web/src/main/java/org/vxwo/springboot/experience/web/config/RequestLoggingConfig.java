package org.vxwo.springboot.experience.web.config;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.vxwo.springboot.experience.web.ConfigPrefix;
import lombok.Data;

/**
 * @author vxwo-team
 */

@Data
@Configuration
@ConfigurationProperties(prefix = ConfigPrefix.REQUEST_LOGGING)
public class RequestLoggingConfig {
    private boolean ignoreRequestHeaders;
    private boolean ignoreResponseHeaders;
    private int responseBodyLimitKb;
    private int stacktraceLimitLines;
    private List<String> includePaths;
}

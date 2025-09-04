package org.vxwo.springboot.experience.web.config;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.vxwo.springboot.experience.web.ConfigPrefix;
import lombok.Data;

/**
 * @author vxwo-team
 */

@Data
@ConfigurationProperties(prefix = ConfigPrefix.REQUEST_LOGGING)
public class RequestLoggingProperties {
    private boolean ignoreRequestHeaders;
    private List<String> requestHeaderKeys;
    private boolean ignoreResponseHeaders;
    private List<String> responseHeaderKeys;

    private boolean saveRequestBodyText;
    private int responseBodyLimitKb;
    private int stacktraceLimitLines;
    private List<String> includePaths;
}

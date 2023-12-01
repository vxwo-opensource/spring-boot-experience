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
@ConfigurationProperties(prefix = ConfigPrefix.FREQUENCY_CONTROL)
public class FrequencyControlConfig {

    @Data
    public static class Concurrency {
        private int durationMs;
        private List<String> includePaths;
    }

    @Data
    public static class FixedInterval {
        private String tag;
        private int durationMs;
        private List<String> includePaths;
    }

    private Concurrency concurrency;
    private List<FixedInterval> fixedIntervals;

}

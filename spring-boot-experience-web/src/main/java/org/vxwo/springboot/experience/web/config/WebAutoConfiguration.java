package org.vxwo.springboot.experience.web.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.vxwo.springboot.experience.web.ConfigPrefix;
import org.vxwo.springboot.experience.web.CoreOrdered;
import org.vxwo.springboot.experience.web.filter.ApiKeyAuthorizationFilter;
import org.vxwo.springboot.experience.web.filter.BearerAuthorizationFilter;
import org.vxwo.springboot.experience.web.filter.CorsFilter;
import org.vxwo.springboot.experience.web.filter.FrequencyControlFilter;
import org.vxwo.springboot.experience.web.filter.RequestLoggingAspect;
import org.vxwo.springboot.experience.web.filter.RequestLoggingFilter;
import org.vxwo.springboot.experience.web.processor.PathProcessor;

/**
 * @author vxwo-team
 */

@Configuration
public class WebAutoConfiguration {

    @Bean
    public PathProcessor pathProcessor() {
        return new PathProcessor();
    }

    @Bean
    @ConditionalOnProperty(value = ConfigPrefix.CORS + ".enabled", havingValue = "true")
    @Order(CoreOrdered.PRELOAD_LAYER)
    public CorsFilter corsFilter(CorsConfig value) {
        return new CorsFilter(value);
    }

    @Bean
    @ConditionalOnProperty(value = ConfigPrefix.REQUEST_LOGGING + ".enabled", havingValue = "true")
    @Order(CoreOrdered.PRELOAD_LAYER + CoreOrdered.LAYER_NEAR)
    public RequestLoggingFilter requestLoggingFilter(RequestLoggingConfig value) {
        return new RequestLoggingFilter(value);
    }

    @Bean
    @ConditionalOnProperty(value = ConfigPrefix.REQUEST_LOGGING + ".enabled", havingValue = "true")
    public RequestLoggingAspect requestLoggingAspect(RequestLoggingConfig value) {
        return new RequestLoggingAspect(value);
    }

    @Bean
    @ConditionalOnProperty(value = ConfigPrefix.AUTHORIZATION_API_KEY + ".enabled",
            havingValue = "true")
    @Order(CoreOrdered.AUTHORIZATION_LAYER)
    public ApiKeyAuthorizationFilter apiKeyAuthorizationFilter(ApiKeyAuthorizationConfig value) {
        return new ApiKeyAuthorizationFilter(value);
    }

    @Bean
    @ConditionalOnProperty(value = ConfigPrefix.AUTHORIZATION_BEARER + ".enabled",
            havingValue = "true")
    @Order(CoreOrdered.AUTHORIZATION_LAYER)
    public BearerAuthorizationFilter bearerAuthorizationFilter(BearerAuthorizationConfig value) {
        return new BearerAuthorizationFilter(value);
    }

    @Bean
    @ConditionalOnProperty(value = ConfigPrefix.FREQUENCY_CONTROL + ".enabled",
            havingValue = "true")
    @Order(CoreOrdered.FREQUENCY_CONTROL_LAYER)
    public FrequencyControlFilter frequencyControlFilter(FrequencyControlConfig value) {
        return new FrequencyControlFilter(value);
    }
}

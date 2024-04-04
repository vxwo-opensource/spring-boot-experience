package org.vxwo.springboot.experience.web.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.vxwo.springboot.experience.web.ConfigPrefix;
import org.vxwo.springboot.experience.web.CoreOrdered;
import org.vxwo.springboot.experience.web.filter.ApiKeyAuthorizationFilter;
import org.vxwo.springboot.experience.web.filter.BearerAuthorizationFilter;
import org.vxwo.springboot.experience.web.filter.CorsFilter;
import org.vxwo.springboot.experience.web.filter.FrequencyControlFilter;
import org.vxwo.springboot.experience.web.filter.ManualAuthorizationFilter;
import org.vxwo.springboot.experience.web.filter.RequestLoggingAspect;
import org.vxwo.springboot.experience.web.filter.RequestLoggingFilter;
import org.vxwo.springboot.experience.web.filter.SecondaryAuthorizationFilter;
import org.vxwo.springboot.experience.web.processor.PathDocumentHelper;
import org.vxwo.springboot.experience.web.processor.PathProcessor;
import org.vxwo.springboot.experience.web.processor.RequestLoggingHelper;

/**
 * @author vxwo-team
 */

public class WebAutoConfiguration {

    @Bean
    public PathProcessor pathProcessor() {
        return new PathProcessor();
    }

    @Bean
    public PathDocumentHelper pathDocumentHelper() {
        return new PathDocumentHelper();
    }

    @Bean
    @ConditionalOnProperty(value = ConfigPrefix.CORS + ".enabled", havingValue = "true")
    @Order(CoreOrdered.PRELOAD_LAYER)
    public CorsFilter corsFilter(CorsConfig value) {
        return new CorsFilter(value);
    }

    @Bean
    public RequestLoggingHelper requestLoggingHelper(RequestLoggingConfig value) {
        return new RequestLoggingHelper(value);
    }

    @Bean
    @ConditionalOnProperty(value = ConfigPrefix.REQUEST_LOGGING + ".enabled", havingValue = "true")
    @Order(CoreOrdered.PRELOAD_LAYER + 1)
    public RequestLoggingFilter requestLoggingFilter(RequestLoggingConfig value) {
        return new RequestLoggingFilter(value);
    }

    @Bean
    @ConditionalOnProperty(value = ConfigPrefix.REQUEST_LOGGING + ".enabled", havingValue = "true")
    public RequestLoggingAspect requestLoggingAspect() {
        return new RequestLoggingAspect();
    }

    @Bean
    @ConditionalOnProperty(value = ConfigPrefix.AUTHORIZATION_API_KEY + ".enabled",
            havingValue = "true")
    @Order(CoreOrdered.FIRST_AUTHORIZATION_LAYER + 1)
    public ApiKeyAuthorizationFilter apiKeyAuthorizationFilter(ApiKeyAuthorizationConfig value) {
        return new ApiKeyAuthorizationFilter(value);
    }

    @Bean
    @ConditionalOnProperty(value = ConfigPrefix.AUTHORIZATION_BEARER + ".enabled",
            havingValue = "true")
    @Order(CoreOrdered.FIRST_AUTHORIZATION_LAYER + 2)
    public BearerAuthorizationFilter bearerAuthorizationFilter(BearerAuthorizationConfig value) {
        return new BearerAuthorizationFilter(value);
    }

    @Bean
    @ConditionalOnProperty(value = ConfigPrefix.AUTHORIZATION_MANUAL + ".enabled",
            havingValue = "true")
    @Order(CoreOrdered.FIRST_AUTHORIZATION_LAYER + 3)
    public ManualAuthorizationFilter manualAuthorizationFilter(ManualAuthorizationConfig value) {
        return new ManualAuthorizationFilter(value);
    }

    @Bean
    @ConditionalOnProperty(value = ConfigPrefix.FREQUENCY_CONTROL + ".enabled",
            havingValue = "true")
    @Order(CoreOrdered.FREQUENCY_CONTROL_LAYER)
    public FrequencyControlFilter frequencyControlFilter(FrequencyControlConfig value) {
        return new FrequencyControlFilter(value);
    }

    @Bean
    @ConditionalOnProperty(value = ConfigPrefix.AUTHORIZATION_SECONDARY + ".enabled",
            havingValue = "true")
    @Order(CoreOrdered.SECONDARY_AUTHORIZATION_LAYER)
    public SecondaryAuthorizationFilter secondaryAuthorizationFilter() {
        return new SecondaryAuthorizationFilter();
    }
}

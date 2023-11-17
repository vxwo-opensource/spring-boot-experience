package org.vxwo.springboot.experience.web.filter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.vxwo.springboot.experience.web.ConfigPrefix;
import org.vxwo.springboot.experience.web.CoreOrdered;
import org.vxwo.springboot.experience.web.config.ApiKeyAuthorizationConfig;
import org.vxwo.springboot.experience.web.handler.AuthorizationFailureHandler;
import org.vxwo.springboot.experience.web.matcher.OwnerPathRuleMatcher;
import org.vxwo.springboot.experience.web.processor.PathProcessor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author vxwo-team
 */

@Slf4j
@Component
@ConditionalOnProperty(value = ConfigPrefix.AUTHORIZATION_API_KEY + ".enabled",
        havingValue = "true")
@Order(CoreOrdered.AUTHORIZATION_LAYER)
public class ApiKeyAuthorizationFilter extends OncePerRequestFilter {

    private final List<String> headerKeys;
    private final boolean parseBearer;
    private final List<String> bearerKeys;
    private final OwnerPathRuleMatcher pathRuleMatcher;

    @Autowired
    private PathProcessor pathProcessor;

    @Autowired
    private AuthorizationFailureHandler failureHandler;

    @Autowired
    public ApiKeyAuthorizationFilter(ApiKeyAuthorizationConfig value) {
        headerKeys = value.getHeaderKeys().stream().map(o -> o.trim()).filter(o -> !o.isEmpty())
                .collect(Collectors.toList());
        parseBearer = value.isParseBearer();
        bearerKeys = value.getBearerKeys();
        pathRuleMatcher = new OwnerPathRuleMatcher(
                ConfigPrefix.AUTHORIZATION_API_KEY + ".path-rules", value.getPathRules());

        if (log.isInfoEnabled()) {
            log.info(getClass().getSimpleName() + " actived, " + pathRuleMatcher.toString());
        }
    }

    @SuppressWarnings("PMD")
    private String parseApiKeyFromHeader(HttpServletRequest request) {
        String apiKey = null;

        for (String key : headerKeys) {
            apiKey = request.getHeader(key);
            if (apiKey != null) {
                break;
            }
        }

        if (apiKey == null && parseBearer) {
            String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authorization != null) {
                String[] fields = authorization.split(" ");
                if (fields.length > 1 && bearerKeys.contains(fields[0])) {
                    apiKey = fields[1];
                }
            }
        }

        return apiKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String matchPath = pathRuleMatcher.findMatchPath(pathProcessor.getRelativeURI(request));
        if (matchPath == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String keyOwner = null;
        String apiKey = parseApiKeyFromHeader(request);
        if (apiKey != null) {
            keyOwner = pathRuleMatcher.findKeyOwner(matchPath, apiKey);
        }

        if (keyOwner != null) {
            filterChain.doFilter(request, response);
        } else {
            failureHandler.handleAuthorizationFailure(request, response, matchPath,
                    "Invalid API Key");
        }
    }
}

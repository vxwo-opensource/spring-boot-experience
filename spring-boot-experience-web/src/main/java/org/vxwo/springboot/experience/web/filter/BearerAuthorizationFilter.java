package org.vxwo.springboot.experience.web.filter;

import java.io.IOException;
import java.util.List;
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
import org.vxwo.springboot.experience.web.config.BearerAuthorizationConfig;
import org.vxwo.springboot.experience.web.handler.AuthorizationFailureHandler;
import org.vxwo.springboot.experience.web.handler.BearerAuthorizationHandler;
import org.vxwo.springboot.experience.web.matcher.GroupPathRuleMatcher;
import org.vxwo.springboot.experience.web.processor.PathProcessor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author vxwo-team
 */

@Slf4j
@Component
@ConditionalOnProperty(value = ConfigPrefix.AUTHORIZATION_BEARER + ".enabled", havingValue = "true")
@Order(CoreOrdered.AUTHORIZATION_LAYER)
public class BearerAuthorizationFilter extends OncePerRequestFilter {

    private final List<String> bearerKeys;
    private final GroupPathRuleMatcher pathRuleMatcher;

    @Autowired
    private PathProcessor pathProcessor;

    @Autowired
    private BearerAuthorizationHandler processHandler;

    @Autowired
    private AuthorizationFailureHandler failureHandler;

    @Autowired
    public BearerAuthorizationFilter(BearerAuthorizationConfig value) {
        bearerKeys = value.getBearerKeys();
        pathRuleMatcher = new GroupPathRuleMatcher(
                ConfigPrefix.AUTHORIZATION_BEARER + ".path-rules", value.getPathRules());

        if (log.isInfoEnabled()) {
            log.info(getClass().getSimpleName() + " actived, " + pathRuleMatcher.toString());
        }
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

        String bearerToken = null;
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization != null) {
            String[] fields = authorization.split(" ");
            if (fields.length > 1 && bearerKeys.contains(fields[0])) {
                bearerToken = fields[1];
            }
        }

        if (bearerToken == null) {
            failureHandler.handleAuthorizationFailure(request, response, matchPath,
                    "empty-bearer-token");
        } else {
            if (processHandler.processBearerToken(request, response, matchPath, bearerToken)) {
                filterChain.doFilter(request, response);
            } else {
                failureHandler.handleAuthorizationFailure(request, response, matchPath,
                        "invalid-bearer-handle");
            }
        }
    }

}

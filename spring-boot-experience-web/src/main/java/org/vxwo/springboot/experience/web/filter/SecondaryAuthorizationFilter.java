package org.vxwo.springboot.experience.web.filter;

import java.io.IOException;
import java.util.UUID;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.filter.OncePerRequestFilter;
import org.vxwo.springboot.experience.web.handler.AuthorizationFailureHandler;
import org.vxwo.springboot.experience.web.handler.SecondaryAuthorizationHandler;
import org.vxwo.springboot.experience.web.matcher.TagPathTester;
import lombok.extern.slf4j.Slf4j;

/**
 * @author vxwo-team
 */

@Slf4j
public class SecondaryAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private AuthorizationFailureHandler failureHandler;

    @Autowired
    private SecondaryAuthorizationHandler processHandler;

    public SecondaryAuthorizationFilter() {
        if (log.isInfoEnabled()) {
            log.info("Secondary authorization actived");
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        TagPathTester<?> tester = AuthorizationHelper.getProcessTester(request);
        if (tester == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (processHandler.processSecondaryAuthorization(request, response)) {
            filterChain.doFilter(request, response);
        } else {
            failureHandler.handleAuthorizationFailure(request, response, tester.getTag(),
                    tester.getPath(), "invalid-secondary-handle");
        }
    }
}


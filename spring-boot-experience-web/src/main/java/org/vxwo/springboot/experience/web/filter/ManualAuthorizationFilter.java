package org.vxwo.springboot.experience.web.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.filter.OncePerRequestFilter;
import org.vxwo.springboot.experience.web.ConfigPrefix;
import org.vxwo.springboot.experience.web.config.ManualAuthorizationConfig;
import org.vxwo.springboot.experience.web.handler.AuthorizationFailureHandler;
import org.vxwo.springboot.experience.web.handler.ManualAuthorizationHandler;
import org.vxwo.springboot.experience.web.matcher.TagPathTester;
import org.vxwo.springboot.experience.web.matcher.GroupPathRuleMatcher;
import org.vxwo.springboot.experience.web.matcher.PathRuleMatcher;
import org.vxwo.springboot.experience.web.processor.PathProcessor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author vxwo-team
 */

@Slf4j
public class ManualAuthorizationFilter extends OncePerRequestFilter {

    private final GroupPathRuleMatcher pathRuleMatcher;

    @Autowired
    private PathProcessor pathProcessor;

    @Autowired
    private ManualAuthorizationHandler processHandler;

    @Autowired
    private AuthorizationFailureHandler failureHandler;

    public ManualAuthorizationFilter(ManualAuthorizationConfig value) {
        pathRuleMatcher = new GroupPathRuleMatcher(
                ConfigPrefix.AUTHORIZATION_BEARER + ".path-rules", value.getPathRules());

        if (log.isInfoEnabled()) {
            log.info("Manual authorization actived, " + pathRuleMatcher.toString());
        }
    }

    public PathRuleMatcher getPathRuleMatcher() {
        return pathRuleMatcher;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String relativePath = pathProcessor.getRelativeURI(request);

        TagPathTester<GroupPathRuleMatcher.ExcludesAndOptionals> tester =
                pathRuleMatcher.findMatchTester(relativePath);
        if (tester == null || tester.getExtra().isExclude(relativePath)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (processHandler.processManualAuthorization(request, response, tester.getTag(),
                tester.getPath()) || tester.getExtra().isOptional(relativePath)) {
            filterChain.doFilter(request, response);
        } else {
            failureHandler.handleAuthorizationFailure(request, response, tester.getTag(),
                    tester.getPath(), "invalid-manual-handle");
        }
    }

}

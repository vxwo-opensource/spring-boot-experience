package org.vxwo.springboot.experience.web.filter;

import java.io.IOException;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.filter.OncePerRequestFilter;
import org.vxwo.springboot.experience.web.ConfigPrefix;
import org.vxwo.springboot.experience.web.config.BearerAuthorizationConfig;
import org.vxwo.springboot.experience.web.handler.AuthorizationFailureHandler;
import org.vxwo.springboot.experience.web.handler.BearerAuthorizationHandler;
import org.vxwo.springboot.experience.web.matcher.TagPathTester;
import org.vxwo.springboot.experience.web.matcher.GroupPathRuleMatcher;
import org.vxwo.springboot.experience.web.matcher.PathRuleMatcher;
import org.vxwo.springboot.experience.web.processor.PathProcessor;
import org.vxwo.springboot.experience.web.util.SplitUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author vxwo-team
 */

@Slf4j
public class BearerAuthorizationFilter extends OncePerRequestFilter {

    private final List<String> bearerKeys;
    private final GroupPathRuleMatcher pathRuleMatcher;

    @Autowired
    private PathProcessor pathProcessor;

    @Autowired
    private BearerAuthorizationHandler processHandler;

    @Autowired
    private AuthorizationFailureHandler failureHandler;

    public BearerAuthorizationFilter(BearerAuthorizationConfig value) {
        bearerKeys = value.getBearerKeys();
        pathRuleMatcher = new GroupPathRuleMatcher(
                ConfigPrefix.AUTHORIZATION_BEARER + ".path-rules", value.getPathRules());

        if (log.isInfoEnabled()) {
            log.info("Bearer authorization actived, " + pathRuleMatcher.toString());
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

        String bearerToken = null;
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization != null) {
            List<String> fields = SplitUtil.splitToList(authorization, " ");
            if (fields.size() > 1 && bearerKeys.contains(fields.get(0))) {
                bearerToken = fields.get(1);
            }
        }

        // Skip the optional path, when no token
        if (bearerToken == null && tester.getExtra().isOptional(relativePath)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (bearerToken == null) {
            failureHandler.handleAuthorizationFailure(request, response, tester.getTag(),
                    tester.getPath(), "no-bearer-token");
        } else {
            if (processHandler.processBearerAuthorization(request, response, tester.getTag(),
                    tester.getPath(), bearerToken)) {
                filterChain.doFilter(request, response);
            } else {
                failureHandler.handleAuthorizationFailure(request, response, tester.getTag(),
                        tester.getPath(), "invalid-bearer-handle");
            }
        }
    }

}

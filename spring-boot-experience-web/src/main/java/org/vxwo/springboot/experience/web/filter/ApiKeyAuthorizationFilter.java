package org.vxwo.springboot.experience.web.filter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.filter.OncePerRequestFilter;
import org.vxwo.springboot.experience.web.ConfigPrefix;
import org.vxwo.springboot.experience.web.config.ApiKeyAuthorizationProperties;
import org.vxwo.springboot.experience.web.handler.AuthorizationFailureHandler;
import org.vxwo.springboot.experience.web.matcher.TagPathTester;
import org.vxwo.springboot.experience.web.matcher.OwnerPathRuleMatcher;
import org.vxwo.springboot.experience.web.matcher.PathRuleMatcher;
import org.vxwo.springboot.experience.web.processor.PathProcessor;
import org.vxwo.springboot.experience.web.util.SplitUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author vxwo-team
 */

@Slf4j
public class ApiKeyAuthorizationFilter extends OncePerRequestFilter {

    private final List<String> headerKeys;
    private final boolean parseBearer;
    private final List<String> bearerKeys;
    private final OwnerPathRuleMatcher pathRuleMatcher;

    @Autowired
    private PathProcessor pathProcessor;

    @Autowired
    private AuthorizationFailureHandler failureHandler;

    public ApiKeyAuthorizationFilter(ApiKeyAuthorizationProperties value) {
        headerKeys = SplitUtil.shrinkList(value.getHeaderKeys());
        parseBearer = value.isParseBearer();
        bearerKeys = value.getBearerKeys();
        pathRuleMatcher = new OwnerPathRuleMatcher(
                ConfigPrefix.AUTHORIZATION_API_KEY + ".path-rules", value.getPathRules());

        if (log.isInfoEnabled()) {
            log.info("ApiKey authorization actived, " + pathRuleMatcher.toString());
        }
    }

    public PathRuleMatcher getPathRuleMatcher() {
        return pathRuleMatcher;
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
                List<String> fields = SplitUtil.splitToList(authorization, " ");
                if (fields.size() > 1 && bearerKeys.contains(fields.get(0))) {
                    apiKey = fields.get(1);
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

        if (AuthorizationHelper.isProcessed(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        TagPathTester<Map<String, String>> tester =
                pathRuleMatcher.findMatchTester(pathProcessor.getRelativeURI(request));
        if (tester == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String apiKey = parseApiKeyFromHeader(request);
        if (apiKey == null) {
            failureHandler.handleAuthorizationFailure(request, response, tester.getTag(),
                    tester.getPath(), "no-api-key");
        } else {
            String keyOwner = tester.getExtra().get(apiKey);
            if (keyOwner != null) {
                AuthorizationHelper.markProcessed(request, tester);
                filterChain.doFilter(request, response);
            } else {
                failureHandler.handleAuthorizationFailure(request, response, tester.getTag(),
                        tester.getPath(), "invalid-api-key");
            }
        }
    }
}

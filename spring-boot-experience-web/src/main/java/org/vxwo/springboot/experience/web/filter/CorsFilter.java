package org.vxwo.springboot.experience.web.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.vxwo.springboot.experience.web.CoreOrdered;
import lombok.extern.slf4j.Slf4j;

/**
 * @author vxwo-team
 */

@Slf4j
@Component
@ConditionalOnProperty(value = "experience.web.cors.enabled", havingValue = "true")
@Order(CoreOrdered.PRELOAD_LAYER)
public class CorsFilter extends OncePerRequestFilter {
    private final boolean parseReferer;
    private final String firstAllowOrigin;
    private final List<String> acceptAllowOrigins;

    @Autowired
    public CorsFilter(CorsConfig value) {
        parseReferer = value.isParseBearer();

        acceptAllowOrigins = Arrays.asList(value.getAllowOrigin().split(",|;")).stream()
                .map(o -> o.trim()).filter(o -> !o.isEmpty()).collect(Collectors.toList());
        firstAllowOrigin = acceptAllowOrigins.isEmpty() ? null : acceptAllowOrigins.get(0);

        if (log.isInfoEnabled()) {
            log.info("CorsFilter actived");
        }
    }

    @SuppressWarnings("PMD")
    private static String parseOriginFromHeader(HttpServletRequest request, boolean parseReferer) {
        String allowOrigin = request.getHeader("Origin");
        if (StringUtils.hasText(allowOrigin)) {
            return allowOrigin;
        }

        String referer = request.getHeader("Referer");
        if (parseReferer && StringUtils.hasText(referer)) {
            int pos = -1;
            if (referer.startsWith("http://")) {
                pos = 7;
            } else if (referer.startsWith("https://")) {
                pos = 8;
            }

            if (pos != -1) {
                int end = referer.indexOf("/", pos);
                allowOrigin = end == -1 ? referer : referer.substring(0, end);
            }
        }

        if (!StringUtils.hasText(allowOrigin)) {
            allowOrigin = "*";
        }

        return allowOrigin;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String allowOrigin = null;
        boolean requireVaryHeader = false;
        if (acceptAllowOrigins.isEmpty()) {
            requireVaryHeader = true;
            allowOrigin = parseOriginFromHeader(request, parseReferer);
        } else {
            if (acceptAllowOrigins.size() == 1) {
                allowOrigin = firstAllowOrigin;
            } else {
                requireVaryHeader = true;
                allowOrigin = parseOriginFromHeader(request, true);
                if (!acceptAllowOrigins.contains(allowOrigin)) {
                    allowOrigin = firstAllowOrigin;
                }
            }
        }

        if (requireVaryHeader) {
            response.setHeader("Vary", "Origin");
        }

        response.setHeader("Access-Control-Allow-Origin", allowOrigin);
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Expose-Headers", "*");

        filterChain.doFilter(request, response);
    }
}

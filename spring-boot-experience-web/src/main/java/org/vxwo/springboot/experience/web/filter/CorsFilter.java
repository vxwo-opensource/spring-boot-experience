package org.vxwo.springboot.experience.web.filter;

import java.io.IOException;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.vxwo.springboot.experience.web.config.CorsConfig;
import org.vxwo.springboot.experience.web.util.SplitUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author vxwo-team
 */

@Slf4j
public class CorsFilter extends OncePerRequestFilter {
    private final static String ORIGIN_ALL = "*";

    private final boolean parseReferer;
    private final String firstAllowOrigin;
    private final List<String> acceptAllowOrigins;

    public CorsFilter(CorsConfig value) {
        parseReferer = value.isParseReferer();

        acceptAllowOrigins = SplitUtil.shrinkList(value.getAllowOrigins());
        firstAllowOrigin = acceptAllowOrigins.isEmpty() ? null : acceptAllowOrigins.get(0);

        if (log.isInfoEnabled()) {
            log.info("CORS actived");
        }
    }

    @SuppressWarnings("PMD")
    private static String parseOriginFromHeader(HttpServletRequest request, boolean parseReferer) {
        String allowOrigin = request.getHeader(HttpHeaders.ORIGIN);
        if (StringUtils.hasText(allowOrigin)) {
            return allowOrigin;
        }

        String referer = request.getHeader(HttpHeaders.REFERER);
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
            allowOrigin = ORIGIN_ALL;
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

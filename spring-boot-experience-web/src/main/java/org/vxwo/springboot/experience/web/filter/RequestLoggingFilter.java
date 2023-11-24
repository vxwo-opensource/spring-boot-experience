package org.vxwo.springboot.experience.web.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.vxwo.springboot.experience.web.ConfigPrefix;
import org.vxwo.springboot.experience.web.CoreOrdered;
import org.vxwo.springboot.experience.web.config.RequestLoggingConfig;
import org.vxwo.springboot.experience.web.entity.RequestLoggingEntity;
import org.vxwo.springboot.experience.web.handler.RequestLoggingHandler;
import org.vxwo.springboot.experience.web.matcher.PathMatcher;
import org.vxwo.springboot.experience.web.processor.PathProcessor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author vxwo-team
 */

@Slf4j
@Component
@ConditionalOnProperty(value = ConfigPrefix.REQUEST_LOGGING + ".enabled", havingValue = "true")
@Order(CoreOrdered.PRELOAD_LAYER + CoreOrdered.LAYER_NEAR)
public class RequestLoggingFilter extends OncePerRequestFilter {

    private final boolean ignoreRequestHeaders;
    private final boolean ignoreResponseHeaders;
    private final int responseBodyLimit;
    private final List<PathMatcher> includePaths;

    @Autowired
    private PathProcessor pathProcessor;

    @Autowired
    private RequestLoggingHandler processHandler;

    @Autowired
    public RequestLoggingFilter(RequestLoggingConfig value) {
        ignoreRequestHeaders = value.isIgnoreRequestHeaders();
        ignoreResponseHeaders = value.isIgnoreResponseHeaders();
        responseBodyLimit = value.getResponseBodyLimitKb() * 1024;
        includePaths = new ArrayList<>();
        for (String line : value.getIncludePaths()) {
            String target = line.trim();
            if (target.isEmpty()) {
                continue;
            }
            includePaths.add(new PathMatcher(target));
        }

        if (log.isInfoEnabled()) {
            StringBuffer sb = new StringBuffer();
            sb.append("Request loggging actived, " + includePaths.size() + " paths");
            for (PathMatcher s : includePaths) {
                sb.append("\n " + s.getTarget());
            }

            log.info(sb.toString());
        }
    }

    private static boolean isIp(String ip) {
        return ip != null && ip.length() > 0 && !"unknown".equalsIgnoreCase(ip);
    }

    private static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (!isIp(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (!isIp(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (!isIp(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (!isIp(ip)) {
            ip = request.getRemoteAddr();
        }
        return isIp(ip) ? ip : "";
    }

    private String parseResponseBody(ContentCachingResponseWrapper response) {
        if (response.getContentSize() <= responseBodyLimit) {
            return "@ignore: size gt " + responseBodyLimit;
        }

        boolean isText = false, isJson = false;
        String contentType = response.getContentType();
        if (contentType == null) {
            isText = true;
        } else {
            isText = contentType.startsWith("text/");
            isJson = contentType.startsWith("application/json");
        }

        if (isText || isJson) {
            String responseText =
                    new String(response.getContentAsByteArray(), StandardCharsets.UTF_8);
            return responseText;
        } else {
            return "@byte[" + response.getContentSize() + "]";
        }

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String relativePath = pathProcessor.getRelativeURI(request);

        PathMatcher matcher = null;
        for (PathMatcher s : includePaths) {
            if (s.match(relativePath)) {
                matcher = s;
                break;
            }
        }
        if (matcher == null) {
            filterChain.doFilter(request, response);
            return;
        }

        RequestLoggingEntity entity = new RequestLoggingEntity();
        entity.setTimeStart(System.currentTimeMillis());
        entity.setRequestUri(request.getRequestURI());
        entity.setRequestMethod(request.getMethod());
        entity.setRequestQuery(request.getQueryString());
        entity.setRequestType(request.getContentType());
        entity.setRequestLength(request.getContentLength());
        entity.setClientIp(getClientIp(request));

        if (!ignoreRequestHeaders) {
            Enumeration<String> headers = request.getHeaderNames();
            while (headers.hasMoreElements()) {
                String key = headers.nextElement();
                entity.getRequestHeaders().put(key, request.getHeader(key));
            }
        }

        ContentCachingResponseWrapper cachedResponse = new ContentCachingResponseWrapper(response);
        request.setAttribute(RequestLoggingEntity.ATTRIBUTE_NAME, entity);

        try {
            filterChain.doFilter(request, cachedResponse);
        } finally {
            entity.setResponseStatus(cachedResponse.getStatus());
            entity.setResponseLength(cachedResponse.getContentSize());
            entity.setResponseType(cachedResponse.getContentType());
            entity.setTimeDuration(System.currentTimeMillis() - entity.getTimeStart());

            if (!ignoreResponseHeaders) {
                for (String key : cachedResponse.getHeaderNames()) {
                    entity.getResponseHeaders().put(key, cachedResponse.getHeader(key));
                }
            }

            try {
                if (entity.getResponseBody() == null) {
                    entity.setResponseBody(parseResponseBody(cachedResponse));
                }
            } catch (Exception ex) {
                entity.setResponseBody("@error: Failed on parse response");
            } finally {
                cachedResponse.copyBodyToResponse();
            }

            try {
                processHandler.publishRequestLogging(entity);
            } catch (Exception ex) {
                log.error("publishRequestLogging", ex);
            }
        }
    }

}

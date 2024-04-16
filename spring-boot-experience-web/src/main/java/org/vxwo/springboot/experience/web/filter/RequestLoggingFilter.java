package org.vxwo.springboot.experience.web.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.vxwo.springboot.experience.web.config.RequestLoggingProperties;
import org.vxwo.springboot.experience.web.entity.RequestLoggingEntity;
import org.vxwo.springboot.experience.web.handler.RequestLoggingHandler;
import org.vxwo.springboot.experience.web.matcher.PathTester;
import org.vxwo.springboot.experience.web.processor.PathProcessor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author vxwo-team
 */

@Slf4j
public class RequestLoggingFilter extends OncePerRequestFilter {

    private final boolean ignoreRequestHeaders;
    private final boolean includeRequestHeaderAllKey;
    private final List<String> includeRequestHeaderKeys;

    private final boolean ignoreResponseHeaders;
    private final boolean includeResponseHeaderAllKey;
    private final List<String> includeResponseHeaderKeys;

    private final int responseBodyLimit;
    private final List<PathTester> includePaths;

    @Value("${spring.application.name:unknow}")
    private String applicationName;

    @Autowired
    private PathProcessor pathProcessor;

    @Autowired
    private RequestLoggingHandler processHandler;

    public RequestLoggingFilter(RequestLoggingProperties value) {
        Set<String> requestHeaderKeys = new HashSet<>();
        if (ObjectUtils.isEmpty(value.getRequestHeaderKeys())
                || value.getRequestHeaderKeys().contains("*")) {
            includeRequestHeaderAllKey = true;
        } else {
            includeRequestHeaderAllKey = false;
            value.getRequestHeaderKeys().forEach(o -> {
                requestHeaderKeys.add(o.toUpperCase());
            });
        }
        ignoreRequestHeaders = value.isIgnoreRequestHeaders();
        includeRequestHeaderKeys = Collections.unmodifiableList(new ArrayList<>(requestHeaderKeys));

        Set<String> responseHeaderKeys = new HashSet<>();
        if (ObjectUtils.isEmpty(value.getResponseHeaderKeys())
                || value.getResponseHeaderKeys().contains("*")) {
            includeResponseHeaderAllKey = true;
        } else {
            includeResponseHeaderAllKey = false;
            value.getResponseHeaderKeys().forEach(o -> {
                responseHeaderKeys.add(o.toUpperCase());
            });
        }
        ignoreResponseHeaders = value.isIgnoreResponseHeaders();
        includeResponseHeaderKeys =
                Collections.unmodifiableList(new ArrayList<>(responseHeaderKeys));

        responseBodyLimit = value.getResponseBodyLimitKb() * 1024;

        includePaths = new ArrayList<>();
        for (String path : value.getIncludePaths()) {
            if (ObjectUtils.isEmpty(path)) {
                continue;
            }
            includePaths.add(new PathTester(path));
        }

        if (log.isInfoEnabled()) {
            StringBuffer sb = new StringBuffer();
            sb.append("Request logging actived, " + includePaths.size() + " paths");
            for (PathTester s : includePaths) {
                sb.append("\n " + s.toPathMatch());
            }

            log.info(sb.toString());
        }
    }

    private static boolean isIp(String ip) {
        return StringUtils.hasText(ip) && !"unknown".equalsIgnoreCase(ip);
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

    private static String getRequestHost(HttpServletRequest request) {
        String host = request.getHeader("X-Forwarded-Host");
        if (host == null) {
            host = request.getHeader(HttpHeaders.HOST);
        }
        if (host == null) {
            host = request.getServerName();
        }
        return StringUtils.hasText(host) ? host : "";
    }

    private String parseResponseBody(ContentCachingResponseWrapper response) {
        int contentSize = response.getContentSize();
        if (contentSize < 1) {
            return "@ignore: empty";
        }
        if (contentSize >= responseBodyLimit) {
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
            return "@byte[" + contentSize + "]";
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

        PathTester matcher = null;
        for (PathTester s : includePaths) {
            if (s.test(relativePath)) {
                matcher = s;
                break;
            }
        }
        if (matcher == null) {
            filterChain.doFilter(request, response);
            return;
        }

        RequestLoggingEntity entity = new RequestLoggingEntity();
        entity.setApplication(applicationName);
        entity.setTimeStart(System.currentTimeMillis());
        entity.setRequestHost(getRequestHost(request));
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
                if (includeRequestHeaderAllKey
                        || includeRequestHeaderKeys.contains(key.toUpperCase())) {
                    entity.getRequestHeaders().put(key, request.getHeader(key));
                }
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
                    if (includeResponseHeaderAllKey
                            || includeResponseHeaderKeys.contains(key.toUpperCase())) {
                        entity.getResponseHeaders().put(key, cachedResponse.getHeader(key));
                    }
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

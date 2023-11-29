package org.vxwo.springboot.experience.web.filter;

import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.filter.OncePerRequestFilter;
import org.vxwo.springboot.experience.web.config.FrequencyControlConfig;
import org.vxwo.springboot.experience.web.handler.FrequencyControlFailureHandler;
import org.vxwo.springboot.experience.web.handler.FrequencyControlHandler;
import org.vxwo.springboot.experience.web.matcher.ExtraPathMatcher;
import org.vxwo.springboot.experience.web.matcher.PathMatcher;
import org.vxwo.springboot.experience.web.processor.PathProcessor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author vxwo-team
 */

@Slf4j
public class FrequencyControlFilter extends OncePerRequestFilter {

    private final Duration concurrencyDuration;
    private final List<PathMatcher> concurrencyIncludePaths;
    private final List<ExtraPathMatcher<Duration>> fixedIntervals;

    @Autowired
    private PathProcessor pathProcessor;

    @Autowired
    private FrequencyControlHandler processHandler;

    @Autowired
    private FrequencyControlFailureHandler failureHandler;

    public FrequencyControlFilter(FrequencyControlConfig value) {
        concurrencyDuration = Duration.ofMillis(value.getConcurrency().getDurationMs());
        concurrencyIncludePaths = new ArrayList<>();
        for (String line : value.getConcurrency().getIncludePaths()) {
            String target = line.trim();
            if (target.isEmpty()) {
                continue;
            }
            concurrencyIncludePaths.add(new PathMatcher(target));
        }

        fixedIntervals = new ArrayList<>();
        for (FrequencyControlConfig.FixedInterval s : value.getFixedIntervals()) {
            Duration duration = Duration.ofMillis(s.getDurationMs());
            for (String line : s.getIncludePaths()) {
                String target = line.trim();
                if (target.isEmpty()) {
                    continue;
                }
                fixedIntervals.add(new ExtraPathMatcher<>(target, duration));
            }
        }

        if (log.isInfoEnabled()) {
            StringBuffer sb = new StringBuffer();
            sb.append("Request frequency control actived");

            sb.append("\n fixed-intervals:");
            if (fixedIntervals.isEmpty()) {
                sb.append(" disabled");
            } else {
                for (ExtraPathMatcher<Duration> s : fixedIntervals) {
                    sb.append("\n  duration: " + s.getExtra().toMillis() + "ms, path: "
                            + s.getTarget());
                }
            }

            sb.append("\n concurrency:");
            if (concurrencyIncludePaths.isEmpty()) {
                sb.append(" disabled");
            } else {
                sb.append("\n  duration: " + concurrencyDuration.toMillis() + "ms");
                sb.append("\n  include-paths:" + String.join("", concurrencyIncludePaths.stream()
                        .map((o) -> "\n   " + o.getTarget()).collect(Collectors.toList())));
            }
            log.info(sb.toString());
        }
    }

    private static String pathToKeyPrefix(String path) {
        return "frequency-control:" + path.replaceAll("/", "_");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String method = request.getMethod();
        String relativePath = pathProcessor.getRelativeURI(request);

        ExtraPathMatcher<Duration> fixedInterval = null;
        for (ExtraPathMatcher<Duration> s : fixedIntervals) {
            if (s.match(relativePath)) {
                fixedInterval = s;
                break;
            }
        }

        String pathKeyPrefix = null;

        if (fixedInterval != null) {
            if (pathKeyPrefix == null) {
                pathKeyPrefix = pathToKeyPrefix(relativePath);
            }
            if (processHandler.obtainFixedInterval(request, response, pathKeyPrefix,
                    fixedInterval.getExtra())) {
                filterChain.doFilter(request, response);
            } else {
                failureHandler.handleFrequencyControlFailure(request, response, method,
                        fixedInterval.getTarget(), "reject-fixed-interval");
            }

            return;
        }

        PathMatcher concurrency = null;
        for (PathMatcher s : concurrencyIncludePaths) {
            if (s.match(relativePath)) {
                concurrency = s;
                break;
            }
        }
        if (concurrency == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (pathKeyPrefix == null) {
            pathKeyPrefix = pathToKeyPrefix(relativePath);
        }

        Map<String, Object> localContext = new HashMap<>(10);
        if (processHandler.enterConcurrency(request, response, pathKeyPrefix, concurrencyDuration,
                localContext)) {
            try {
                filterChain.doFilter(request, response);
            } finally {
                processHandler.leaveConcurrency(request, response, localContext);
            }
        } else {
            failureHandler.handleFrequencyControlFailure(request, response, method,
                    concurrency.getTarget(), "reject-concurrency");
        }
    }
}

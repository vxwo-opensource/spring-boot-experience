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
import org.vxwo.springboot.experience.web.ConfigPrefix;
import org.vxwo.springboot.experience.web.config.FrequencyControlProperties;
import org.vxwo.springboot.experience.web.handler.FrequencyControlFailureHandler;
import org.vxwo.springboot.experience.web.handler.FrequencyControlHandler;
import org.vxwo.springboot.experience.web.matcher.TagPathTester;
import org.vxwo.springboot.experience.web.matcher.PathTester;
import org.vxwo.springboot.experience.web.processor.PathProcessor;
import org.vxwo.springboot.experience.web.util.SplitUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author vxwo-team
 */

@Slf4j
public class FrequencyControlFilter extends OncePerRequestFilter {

    private final Duration concurrencyDuration;
    private final List<PathTester> concurrencyIncludePaths;
    private final List<TagPathTester<Duration>> fixedIntervals;

    @Autowired
    private PathProcessor pathProcessor;

    @Autowired
    private FrequencyControlHandler processHandler;

    @Autowired
    private FrequencyControlFailureHandler failureHandler;

    public FrequencyControlFilter(FrequencyControlProperties value) {
        if (value.getConcurrency().getDurationMs() < 1) {
            throw new RuntimeException(
                    String.format("Configuration: {%s.concurrency.duration-ms} less then 1",
                            ConfigPrefix.FREQUENCY_CONTROL));
        }

        concurrencyDuration = Duration.ofMillis(value.getConcurrency().getDurationMs());
        concurrencyIncludePaths = new ArrayList<>();
        for (String path : SplitUtil.shrinkList(value.getConcurrency().getIncludePaths())) {
            if (PathTester.isPattern(path)) {
                throw new RuntimeException(String.format(
                        "Configuration: {%s.concurrency.include-paths} has pattern character",
                        ConfigPrefix.FREQUENCY_CONTROL));
            }
            concurrencyIncludePaths.add(new PathTester(path));
        }

        fixedIntervals = new ArrayList<>();
        for (int i = 0; i < value.getFixedIntervals().size(); ++i) {
            FrequencyControlProperties.FixedInterval s = value.getFixedIntervals().get(i);
            String configPathName =
                    String.format("%s.fixed-intervals.[%d]", ConfigPrefix.FREQUENCY_CONTROL, i);

            if (s.getDurationMs() < 1) {
                throw new RuntimeException(String
                        .format("Configuration: {%s.duration-ms} less then 1", configPathName));
            }

            Duration duration = Duration.ofMillis(s.getDurationMs());
            for (String path : SplitUtil.shrinkList(s.getIncludePaths())) {
                if (PathTester.isPattern(path)) {
                    throw new RuntimeException(
                            String.format("Configuration: {%s.include-paths} has pattern character",
                                    configPathName));
                }
                fixedIntervals.add(new TagPathTester<>(s.getTag(), path, duration));
            }
        }

        if (log.isInfoEnabled()) {
            StringBuffer sb = new StringBuffer();
            sb.append("Request frequency control actived");

            sb.append("\n fixed-intervals:");
            if (fixedIntervals.isEmpty()) {
                sb.append(" disabled");
            } else {
                for (TagPathTester<Duration> s : fixedIntervals) {
                    sb.append("\n  tag: " + s.getTag() + ", duration: " + s.getExtra().toMillis()
                            + "ms, path: " + s.toPathMatch());
                }
            }

            sb.append("\n concurrency:");
            if (concurrencyIncludePaths.isEmpty()) {
                sb.append(" disabled");
            } else {
                sb.append("\n  duration: " + concurrencyDuration.toMillis() + "ms");
                sb.append("\n  include-paths:" + String.join("", concurrencyIncludePaths.stream()
                        .map((o) -> "\n   " + o.toPathMatch()).collect(Collectors.toList())));
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

        TagPathTester<Duration> fixedInterval = null;
        for (TagPathTester<Duration> s : fixedIntervals) {
            if (s.test(relativePath)) {
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
                failureHandler.handleFrequencyControlFixedIntervalFailure(request, response, method,
                        fixedInterval.getTag(), fixedInterval.getPath());
            }

            return;
        }

        PathTester concurrency = null;
        for (PathTester s : concurrencyIncludePaths) {
            if (s.test(relativePath)) {
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
            failureHandler.handleFrequencyControlConcurrencyFailure(request, response, method,
                    concurrency.getPath());
        }
    }
}

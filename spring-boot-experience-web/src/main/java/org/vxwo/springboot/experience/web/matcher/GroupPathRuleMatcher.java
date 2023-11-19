package org.vxwo.springboot.experience.web.matcher;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.springframework.util.ObjectUtils;

/**
 * @author vxwo-team
 *
 * The path rule line example:
 * path;exclude-sub-1,exclude-sub-2;optinal-sub-1,optional-sub-1
 */

public class GroupPathRuleMatcher {
    public final static String GROUP_SEPARATOR = ";";
    public final static String PATHS_SEPARATOR = ",";

    public final static int GROUP_EXCLUDE_PATHS = 1;
    public final static int GROUP_OPTIONAL_PATHS = 2;

    private final List<PathMatcher> acceptPaths;
    private final Map<String, List<PathMatcher>> excludePathMatcherMap;
    private final Map<String, List<PathMatcher>> optionalPathMatcherMap;

    public GroupPathRuleMatcher(String configName, List<String> pathRules) {
        acceptPaths = new ArrayList<>();
        excludePathMatcherMap = new ConcurrentHashMap<>();
        optionalPathMatcherMap = new ConcurrentHashMap<>();

        if (ObjectUtils.isEmpty(pathRules)) {
            throw new RuntimeException("Configuration: [" + configName + "] Empty");
        }

        Set<String> excludeOrOptionalPathSet = new HashSet<>();
        for (String line : pathRules) {
            String target = line.trim();
            if (target.isEmpty()) {
                continue;
            }

            String[] fields = target.split(GROUP_SEPARATOR);
            String path = fields[0].trim();
            if (path.isEmpty()) {
                continue;
            }

            if (!path.endsWith("/")) {
                path += "/";
            }

            List<PathMatcher> excludePathMatchers = new ArrayList<>();
            if (fields.length > GROUP_EXCLUDE_PATHS) {
                for (String o : fields[GROUP_EXCLUDE_PATHS].split(PATHS_SEPARATOR)) {
                    String exclude = o.trim();
                    if (exclude.isEmpty()) {
                        continue;
                    }

                    if (exclude.startsWith("/")) {
                        throw new RuntimeException(
                                "Configuration: [" + configName + "] Failed on path: " + path
                                        + " exlude starts with '/': " + exclude);
                    }

                    String excludePath = path + exclude;
                    PathMatcher matcher = new PathMatcher(excludePath);
                    if (!excludeOrOptionalPathSet.contains(excludePath)) {
                        excludePathMatchers.add(matcher);
                        excludeOrOptionalPathSet.add(excludePath);
                    }
                }
            }

            List<PathMatcher> optionalPathMatchers = new ArrayList<>();
            if (fields.length > GROUP_OPTIONAL_PATHS) {
                for (String o : fields[GROUP_OPTIONAL_PATHS].split(PATHS_SEPARATOR)) {
                    String optional = o.trim();
                    if (optional.isEmpty()) {
                        continue;
                    }

                    if (optional.startsWith("/")) {
                        throw new RuntimeException(
                                "Configuration: [" + configName + "] failed on path: " + path
                                        + " optional starts with '/': " + optional);
                    }

                    String optionalPath = path + optional;
                    PathMatcher matcher = new PathMatcher(optionalPath);
                    if (!excludeOrOptionalPathSet.contains(optionalPath)) {
                        optionalPathMatchers.add(matcher);
                        excludeOrOptionalPathSet.add(optionalPath);
                    }
                }
            }

            acceptPaths.add(new PathMatcher(path));
            excludePathMatcherMap.put(path, excludePathMatchers);
            optionalPathMatcherMap.put(path, optionalPathMatchers);
        }
    }

    public String findMatchPath(String path) {
        String matchPath = null;
        for (PathMatcher s : acceptPaths) {
            if (s.match(path)) {
                matchPath = s.getTarget();
                break;
            }
        }

        if (matchPath != null) {
            for (PathMatcher s : excludePathMatcherMap.get(matchPath)) {
                if (s.match(path)) {
                    matchPath = null;
                    break;
                }
            }
        }

        return matchPath;
    }

    public boolean isOptionalPath(String matchPath, String path) {
        for (PathMatcher s : optionalPathMatcherMap.get(matchPath)) {
            if (s.match(path)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(acceptPaths.size() + " paths");
        for (PathMatcher s : acceptPaths) {
            sb.append("\n path: " + s.getTarget());

            List<PathMatcher> pathMatchers = excludePathMatcherMap.get(s.getTarget());
            if (!pathMatchers.isEmpty()) {
                sb.append(", exclude: " + String.join(",", pathMatchers.stream()
                        .map(o -> o.getTarget()).collect(Collectors.toList())));
            }

            pathMatchers = optionalPathMatcherMap.get(s.getTarget());
            if (!pathMatchers.isEmpty()) {
                sb.append(", optional: " + String.join(",", pathMatchers.stream()
                        .map(o -> o.getTarget()).collect(Collectors.toList())));
            }
        }

        return sb.toString();
    }
}

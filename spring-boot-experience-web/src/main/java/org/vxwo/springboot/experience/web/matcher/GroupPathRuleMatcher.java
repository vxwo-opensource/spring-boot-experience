package org.vxwo.springboot.experience.web.matcher;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.springframework.util.ObjectUtils;
import org.vxwo.springboot.experience.web.config.GroupPathRule;
import org.vxwo.springboot.experience.web.util.SplitUtil;

/**
 * @author vxwo-team
 *
 * The path rule line example:
 * path;exclude-sub-1,exclude-sub-2;optinal-sub-1,optional-sub-1
 */

public class GroupPathRuleMatcher {
    private final List<PathTester> acceptPaths;
    private final Map<String, List<PathTester>> excludePathTesterMap;
    private final Map<String, List<PathTester>> optionalPathTesterMap;

    @SuppressWarnings("PMD.AvoidComplexConditionRule")
    public GroupPathRuleMatcher(String configName, List<GroupPathRule> pathRules) {
        acceptPaths = new ArrayList<>();
        excludePathTesterMap = new ConcurrentHashMap<>();
        optionalPathTesterMap = new ConcurrentHashMap<>();

        if (ObjectUtils.isEmpty(pathRules)) {
            throw new RuntimeException("Configuration: [" + configName + "] Empty");
        }

        Set<String> excludeOrOptionalPathSet = new HashSet<>();
        for (GroupPathRule pathRule : pathRules) {
            String path = pathRule.getPath();
            if (path.isEmpty()) {
                continue;
            }

            boolean existExcludes = !ObjectUtils.isEmpty(pathRule.getExcludes());
            boolean existOptionals = !ObjectUtils.isEmpty(pathRule.getOptionals());
            if ((existExcludes || existOptionals) && !path.endsWith("/")) {
                path += "/";
            }

            List<PathTester> excludePathTesters = new ArrayList<>();
            if (existExcludes) {
                for (String exclude : SplitUtil.shrinkList(pathRule.getExcludes())) {
                    if (exclude.startsWith("/")) {
                        throw new RuntimeException(
                                "Configuration: [" + configName + "] Failed on path: " + path
                                        + " exlude starts with '/': " + exclude);
                    }

                    String excludePath = path + exclude;
                    PathTester matcher = new PathTester(excludePath);
                    if (!excludeOrOptionalPathSet.contains(excludePath)) {
                        excludePathTesters.add(matcher);
                        excludeOrOptionalPathSet.add(excludePath);
                    }
                }
            }

            List<PathTester> optionalPathTesters = new ArrayList<>();
            if (existOptionals) {
                for (String optional : SplitUtil.shrinkList(pathRule.getOptionals())) {
                    if (optional.startsWith("/")) {
                        throw new RuntimeException(
                                "Configuration: [" + configName + "] failed on path: " + path
                                        + " optional starts with '/': " + optional);
                    }

                    String optionalPath = path + optional;
                    PathTester matcher = new PathTester(optionalPath);
                    if (!excludeOrOptionalPathSet.contains(optionalPath)) {
                        optionalPathTesters.add(matcher);
                        excludeOrOptionalPathSet.add(optionalPath);
                    }
                }
            }

            acceptPaths.add(new PathTester(path));
            excludePathTesterMap.put(path, excludePathTesters);
            optionalPathTesterMap.put(path, optionalPathTesters);
        }
    }

    public String findMatchPath(String path) {
        String matchPath = null;
        for (PathTester s : acceptPaths) {
            if (s.test(path)) {
                matchPath = s.getTarget();
                break;
            }
        }

        if (matchPath != null) {
            for (PathTester s : excludePathTesterMap.get(matchPath)) {
                if (s.test(path)) {
                    matchPath = null;
                    break;
                }
            }
        }

        return matchPath;
    }

    public boolean isOptionalPath(String matchPath, String path) {
        for (PathTester s : optionalPathTesterMap.get(matchPath)) {
            if (s.test(path)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(acceptPaths.size() + " paths");
        for (PathTester s : acceptPaths) {
            sb.append("\n path: " + s.getTarget());

            List<PathTester> pathMatchers = excludePathTesterMap.get(s.getTarget());
            if (!pathMatchers.isEmpty()) {
                sb.append(", excludes: " + String.join(",", pathMatchers.stream()
                        .map(o -> o.getTarget()).collect(Collectors.toList())));
            }

            pathMatchers = optionalPathTesterMap.get(s.getTarget());
            if (!pathMatchers.isEmpty()) {
                sb.append(", optionals: " + String.join(",", pathMatchers.stream()
                        .map(o -> o.getTarget()).collect(Collectors.toList())));
            }
        }

        return sb.toString();
    }
}

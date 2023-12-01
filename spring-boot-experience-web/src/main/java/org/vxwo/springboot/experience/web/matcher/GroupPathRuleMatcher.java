package org.vxwo.springboot.experience.web.matcher;

import java.util.*;
import java.util.stream.Collectors;
import org.springframework.util.ObjectUtils;
import org.vxwo.springboot.experience.web.config.GroupPathRule;
import org.vxwo.springboot.experience.web.util.SplitUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author vxwo-team
 *
 * The path rule line example:
 * path;exclude-sub-1,exclude-sub-2;optinal-sub-1,optional-sub-1
 */

public class GroupPathRuleMatcher {
    @Getter
    @AllArgsConstructor
    public static class ExcludesAndOptionals {
        private List<PathTester> excludes;
        private List<PathTester> optionals;

        public boolean isExclude(String path) {
            for (PathTester s : excludes) {
                if (s.test(path)) {
                    return true;
                }
            }

            return false;
        }

        public boolean isOptional(String path) {
            for (PathTester s : optionals) {
                if (s.test(path)) {
                    return true;
                }
            }

            return false;
        }
    }

    private final List<ExtraPathTester<ExcludesAndOptionals>> acceptPathTesters;

    @SuppressWarnings("PMD.AvoidComplexConditionRule")
    public GroupPathRuleMatcher(String configName, List<GroupPathRule> pathRules) {
        acceptPathTesters = new ArrayList<>();

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

            acceptPathTesters.add(new ExtraPathTester<>(path,
                    new ExcludesAndOptionals(Collections.unmodifiableList(excludePathTesters),
                            Collections.unmodifiableList(optionalPathTesters))));
        }
    }

    public ExtraPathTester<ExcludesAndOptionals> findMatchTester(String path) {
        for (ExtraPathTester<ExcludesAndOptionals> tester : acceptPathTesters) {
            if (tester.test(path)) {
                return tester;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(acceptPathTesters.size() + " paths");
        for (ExtraPathTester<ExcludesAndOptionals> tester : acceptPathTesters) {
            sb.append("\n path: " + tester.getPath());

            if (!ObjectUtils.isEmpty(tester.getExtra().getExcludes())) {
                sb.append(", excludes: " + String.join(",", tester.getExtra().getExcludes().stream()
                        .map(o -> o.getPath()).collect(Collectors.toList())));
            }

            if (!ObjectUtils.isEmpty(tester.getExtra().getOptionals())) {
                sb.append(", optionals: " + String.join(",", tester.getExtra().getOptionals()
                        .stream().map(o -> o.getPath()).collect(Collectors.toList())));
            }
        }

        return sb.toString();
    }
}

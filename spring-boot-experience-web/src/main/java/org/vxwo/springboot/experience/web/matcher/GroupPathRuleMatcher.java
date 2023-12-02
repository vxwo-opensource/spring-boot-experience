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

    private final List<TagPathTester<ExcludesAndOptionals>> acceptPathTesters;

    @SuppressWarnings("PMD.AvoidComplexConditionRule")
    public GroupPathRuleMatcher(String configName, List<GroupPathRule> pathRules) {
        acceptPathTesters = new ArrayList<>();

        if (ObjectUtils.isEmpty(pathRules)) {
            throw new RuntimeException(String.format("Configuration: {%s} empty", configName));
        }

        Set<String> excludeOrOptionalPathSet = new HashSet<>();
        for (int i = 0; i < pathRules.size(); ++i) {
            GroupPathRule pathRule = pathRules.get(i);
            String configPathName = String.format("%s.[%d]", configName, i);

            String path = pathRule.getPath();
            if (ObjectUtils.isEmpty(path)) {
                throw new RuntimeException(
                        String.format("Configuration: {%s.path} empty", configPathName));
            }

            boolean existExcludes = !ObjectUtils.isEmpty(pathRule.getExcludes());
            boolean existOptionals = !ObjectUtils.isEmpty(pathRule.getOptionals());
            if ((existExcludes || existOptionals) && !path.endsWith("/")) {
                throw new RuntimeException(String
                        .format("Configuration: {%s.path} not starts with '/'", configPathName));
            }

            List<PathTester> excludePathTesters = new ArrayList<>();
            if (existExcludes) {
                for (String exclude : SplitUtil.shrinkList(pathRule.getExcludes())) {
                    if (exclude.startsWith("/")) {
                        throw new RuntimeException(String.format(
                                "Configuration: {%s.excludes} found starts with '/': %s",
                                configPathName, exclude));
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
                        throw new RuntimeException(String.format(
                                "Configuration: {%s.optionals} found starts with '/': %s",
                                configPathName, optional));
                    }

                    String optionalPath = path + optional;
                    PathTester matcher = new PathTester(optionalPath);
                    if (!excludeOrOptionalPathSet.contains(optionalPath)) {
                        optionalPathTesters.add(matcher);
                        excludeOrOptionalPathSet.add(optionalPath);
                    }
                }
            }

            acceptPathTesters.add(new TagPathTester<>(pathRule.getTag(), path,
                    new ExcludesAndOptionals(Collections.unmodifiableList(excludePathTesters),
                            Collections.unmodifiableList(optionalPathTesters))));
        }
    }

    public TagPathTester<ExcludesAndOptionals> findMatchTester(String path) {
        for (TagPathTester<ExcludesAndOptionals> tester : acceptPathTesters) {
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
        for (TagPathTester<ExcludesAndOptionals> tester : acceptPathTesters) {
            sb.append("\ntag: " + tester.getTag() + ", path: " + tester.toPathMatch());

            if (!ObjectUtils.isEmpty(tester.getExtra().getExcludes())) {
                sb.append(", excludes: " + String.join(",", tester.getExtra().getExcludes().stream()
                        .map(o -> o.toPathMatch()).collect(Collectors.toList())));
            }

            if (!ObjectUtils.isEmpty(tester.getExtra().getOptionals())) {
                sb.append(", optionals: " + String.join(",", tester.getExtra().getOptionals()
                        .stream().map(o -> o.toPathMatch()).collect(Collectors.toList())));
            }
        }

        return sb.toString();
    }
}

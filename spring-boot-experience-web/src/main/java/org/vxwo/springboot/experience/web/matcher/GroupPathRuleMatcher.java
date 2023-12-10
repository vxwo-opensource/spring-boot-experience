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

public class GroupPathRuleMatcher implements PathRuleMatcher {
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
            if (PathTester.hasPattern(path)) {
                throw new RuntimeException(String
                        .format("Configuration: {%s.path} has pattern character", configPathName));
            }

            boolean existExcludes = !ObjectUtils.isEmpty(pathRule.getExcludes());
            boolean existOptionals = !ObjectUtils.isEmpty(pathRule.getOptionals());
            if ((existExcludes || existOptionals) && !path.endsWith("/")) {
                throw new RuntimeException(String
                        .format("Configuration: {%s.path} not ends with '/'", configPathName));
            }

            List<PathTester> excludePathTesters = new ArrayList<>();
            if (existExcludes) {
                for (String exclude : SplitUtil.shrinkList(pathRule.getExcludes())) {
                    if (exclude.startsWith("/")) {
                        throw new RuntimeException(String.format(
                                "Configuration: {%s.excludes} starts with '/'", configPathName));
                    }
                    if (PathTester.hasPattern(exclude)) {
                        throw new RuntimeException(
                                String.format("Configuration: {%s.excludes} has pattern character",
                                        configPathName));
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
                                "Configuration: {%s.optionals} starts with '/'", configPathName));
                    }
                    if (PathTester.hasPattern(optional)) {
                        throw new RuntimeException(
                                String.format("Configuration: {%s.optionals} has pattern character",
                                        configPathName));
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

    @Override
    public List<String> getPathMatchs(String tag) {
        return acceptPathTesters.stream().filter(o -> o.getTag().equals(tag))
                .map(o -> o.toPathMatch()).collect(Collectors.toList());
    }

    @Override
    public List<String> getExcludePathMatchs(String tag) {
        List<String> pathMatches = new ArrayList<>();
        acceptPathTesters.stream().filter(o -> o.getTag().equals(tag)).forEach(o -> {
            o.getExtra().getExcludes().stream().forEach(x -> pathMatches.add(x.toPathMatch()));
            o.getExtra().getOptionals().stream().forEach(x -> pathMatches.add(x.toPathMatch()));
        });
        return pathMatches;
    }
}

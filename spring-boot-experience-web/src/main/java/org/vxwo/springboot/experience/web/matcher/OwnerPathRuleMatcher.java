package org.vxwo.springboot.experience.web.matcher;

import java.util.*;
import org.springframework.util.ObjectUtils;
import org.vxwo.springboot.experience.web.config.OwnerPathRule;

/**
 * @author vxwo-team
 */

public class OwnerPathRuleMatcher {
    private final List<TagPathTester<Map<String, String>>> acceptPathTesters;

    public OwnerPathRuleMatcher(String configName, List<OwnerPathRule> pathRules) {
        acceptPathTesters = new ArrayList<>();

        if (ObjectUtils.isEmpty(pathRules)) {
            throw new RuntimeException(String.format("Configuration: {%s} empty", configName));
        }

        for (int i = 0; i < pathRules.size(); ++i) {
            OwnerPathRule pathRule = pathRules.get(i);
            String configPathName = String.format("%s.[%d]", configName, i);

            String path = pathRule.getPath();
            if (ObjectUtils.isEmpty(path)) {
                throw new RuntimeException(
                        String.format("Configuration: {%s.path} empty", configPathName));
            }

            Map<String, String> acceptKeys = new HashMap<String, String>();
            for (OwnerPathRule.KeyOwner target : pathRule.getOwners()) {
                String key = target.getKey();
                if (ObjectUtils.isEmpty(key)) {
                    continue;
                }

                String owner = target.getOwner();
                if (ObjectUtils.isEmpty(owner)) {
                    owner = "none";
                }

                acceptKeys.put(key, owner);
            }

            acceptPathTesters.add(new TagPathTester<>(pathRule.getTag(), path,
                    Collections.unmodifiableMap(acceptKeys)));
        }
    }

    public TagPathTester<Map<String, String>> findMatchTester(String path) {
        for (TagPathTester<Map<String, String>> tester : acceptPathTesters) {
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
        for (TagPathTester<Map<String, String>> tester : acceptPathTesters) {
            sb.append("\ntag: " + tester.getTag() + ", path: " + tester.getPath() + ", owners: "
                    + String.join(",", tester.getExtra().values()));
        }

        return sb.toString();
    }
}

package org.vxwo.springboot.experience.web.matcher;

import java.util.*;
import org.springframework.util.ObjectUtils;
import org.vxwo.springboot.experience.web.config.OwnerPathRule;

/**
 * @author vxwo-team
 *
 * The path rule example:
 * path;key-1:owner-1;key-2:owner-2;etc.
 */

public class OwnerPathRuleMatcher {
    private final List<ExtraPathTester<Map<String, String>>> acceptPathTesters;

    public OwnerPathRuleMatcher(String configName, List<OwnerPathRule> pathRules) {
        acceptPathTesters = new ArrayList<>();

        if (ObjectUtils.isEmpty(pathRules)) {
            throw new RuntimeException("Configuration: [" + configName + "] Empty");
        }

        for (OwnerPathRule pathRule : pathRules) {
            String path = pathRule.getPath();
            if (ObjectUtils.isEmpty(path.isEmpty())) {
                continue;
            }

            Map<String, String> acceptKeys = new HashMap<String, String>();
            for (OwnerPathRule.KeyOwner target : pathRule.getOwners()) {
                String key = target.getKey();
                if (ObjectUtils.isEmpty(key.isEmpty())) {
                    continue;
                }

                String owner = target.getOwner();
                if (ObjectUtils.isEmpty(key.isEmpty())) {
                    owner = "UNKNOW";
                }

                acceptKeys.put(key, owner);
            }

            acceptPathTesters
                    .add(new ExtraPathTester<>(path, Collections.unmodifiableMap(acceptKeys)));
        }
    }

    public ExtraPathTester<Map<String, String>> findMatchTester(String path) {
        for (ExtraPathTester<Map<String, String>> tester : acceptPathTesters) {
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
        for (ExtraPathTester<Map<String, String>> tester : acceptPathTesters) {
            sb.append("\n path: " + tester.getPath() + ", owners: "
                    + String.join(",", tester.getExtra().values()));
        }

        return sb.toString();
    }
}

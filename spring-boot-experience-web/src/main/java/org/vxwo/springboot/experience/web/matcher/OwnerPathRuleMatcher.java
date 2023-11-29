package org.vxwo.springboot.experience.web.matcher;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.util.ObjectUtils;
import org.vxwo.springboot.experience.web.config.OwnerPathRule;

/**
 * @author vxwo-team
 *
 * The path rule example:
 * path;key-1:owner-1;key-2:owner-2;etc.
 */

public class OwnerPathRuleMatcher {
    private final List<PathMatcher> acceptPaths;
    private final Map<String, Map<String, String>> acceptPathRules;

    public OwnerPathRuleMatcher(String configName, List<OwnerPathRule> pathRules) {
        acceptPaths = new ArrayList<>();
        acceptPathRules = new ConcurrentHashMap<>();

        if (ObjectUtils.isEmpty(pathRules)) {
            throw new RuntimeException("Configuration: [" + configName + "] Empty");
        }

        for (OwnerPathRule pathRule : pathRules) {
            String path = pathRule.getPath();
            if (ObjectUtils.isEmpty(path.isEmpty())) {
                continue;
            }

            Map<String, String> acceptKeys = new ConcurrentHashMap<String, String>();
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

            acceptPaths.add(new PathMatcher(path));
            acceptPathRules.put(path, acceptKeys);
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

        return matchPath;
    }

    public String findKeyOwner(String matchPath, String key) {
        String keyOwner = null;
        if (key != null && !key.isEmpty()) {
            Map<String, String> acceptKeys = acceptPathRules.get(matchPath);
            if (acceptKeys != null) {
                keyOwner = acceptKeys.get(key);
            }
        }

        return keyOwner;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(acceptPaths.size() + " paths");
        for (Map.Entry<String, Map<String, String>> pair : acceptPathRules.entrySet()) {
            sb.append("\n path: " + pair.getKey() + ", owners: "
                    + String.join(",", pair.getValue().values()));
        }

        return sb.toString();
    }
}

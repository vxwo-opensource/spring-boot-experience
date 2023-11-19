package org.vxwo.springboot.experience.web.matcher;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.util.ObjectUtils;

/**
 * @author vxwo-team
 *
 * The path rule example:
 * path;key-1:owner-1;key-2:owner-2;etc.
 */

public class OwnerPathRuleMatcher {
    public final static String GROUP_SEPARATOR = ";";
    public final static String FIELD_SEPARATOR = ":";

    private final List<PathMatcher> acceptPaths;
    private final Map<String, Map<String, String>> acceptPathRules;

    public OwnerPathRuleMatcher(String configName, List<String> pathRules) {
        acceptPaths = new ArrayList<>();
        acceptPathRules = new ConcurrentHashMap<>();

        if (ObjectUtils.isEmpty(pathRules)) {
            throw new RuntimeException("Configuration: [" + configName + "] Empty");
        }

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

            Map<String, String> acceptKeys = new ConcurrentHashMap<String, String>();
            for (int i = 1; i < fields.length; ++i) {
                String field = fields[i].trim();
                if (field.isEmpty()) {
                    continue;
                }

                String[] pair = field.split(FIELD_SEPARATOR);
                String key = pair[0].trim();
                String owner = (pair.length > 1 ? pair[1] : "").trim();

                if (key.isEmpty()) {
                    continue;
                }

                if (owner.isEmpty()) {
                    owner = "unknow";
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

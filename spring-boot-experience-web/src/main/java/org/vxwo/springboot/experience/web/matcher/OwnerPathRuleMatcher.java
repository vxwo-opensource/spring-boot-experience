package org.vxwo.springboot.experience.web.matcher;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.util.ObjectUtils;
import org.vxwo.springboot.experience.web.util.SplitUtil;

/**
 * @author vxwo-team
 *
 * The path rule example:
 * path;key-1:owner-1;key-2:owner-2;etc.
 */

public class OwnerPathRuleMatcher {
    public final static String FIELD_SEPARATOR = ";";
    public final static String PAIR_SEPARATOR = ":";

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

            List<String> fields = SplitUtil.splitToList(target, FIELD_SEPARATOR);
            if (fields.isEmpty()) {
                continue;
            }

            String path = fields.get(0);

            Map<String, String> acceptKeys = new ConcurrentHashMap<String, String>();
            for (int i = 1; i < fields.size(); ++i) {
                String field = fields.get(i);
                if (field.isEmpty()) {
                    continue;
                }

                List<String> pair = SplitUtil.splitToList(field, PAIR_SEPARATOR);
                if (pair.isEmpty()) {
                    continue;
                }

                String key = pair.get(0);
                String owner = pair.size() > 1 ? pair.get(1) : "";
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

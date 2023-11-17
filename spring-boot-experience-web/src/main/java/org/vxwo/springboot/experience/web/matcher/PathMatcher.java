package org.vxwo.springboot.experience.web.matcher;

/**
 * Simple path matcher
 *
 * @author vxwo-team
 */

public class PathMatcher {
    private final static String END_FLAG = "$";

    private final String target;
    private final boolean matchFull;
    private final String matchValue;

    public PathMatcher(String path) {
        target = path;
        matchFull = path.endsWith(END_FLAG);
        matchValue = !matchFull ? path : path.substring(0, path.length() - 1);
    }

    public boolean match(String path) {
        return matchFull ? path.equals(matchValue) : path.startsWith(matchValue);
    }

    public String getTarget() {
        return target;
    }
}

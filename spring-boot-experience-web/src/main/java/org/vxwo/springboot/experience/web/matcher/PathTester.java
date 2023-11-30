package org.vxwo.springboot.experience.web.matcher;

import java.util.function.Predicate;

/**
 * Simple path tester
 *
 * @author vxwo-team
 */

public class PathTester implements Predicate<String> {
    private final static String END_FLAG = "$";

    private final String target;
    private final boolean matchFull;
    private final String matchValue;

    public PathTester(String path) {
        target = path;
        matchFull = path.endsWith(END_FLAG);
        matchValue = !matchFull ? path : path.substring(0, path.length() - 1);
    }

    public String getTarget() {
        return target;
    }

    @Override
    public boolean test(String input) {
        return matchFull ? input.equals(matchValue) : input.startsWith(matchValue);
    }
}

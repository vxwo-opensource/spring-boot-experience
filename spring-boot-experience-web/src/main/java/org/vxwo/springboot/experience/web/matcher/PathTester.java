package org.vxwo.springboot.experience.web.matcher;

import lombok.Getter;

/**
 * Simple Ant style path tester
 *
 * @author vxwo-team
 */

public class PathTester {
    private final static String STUFFIX_FLAG = "/";

    @Getter
    private final String path;
    private final boolean fullMatch;
    private final String matchString;
    private final int matchLength;

    @SuppressWarnings("PMD")
    public PathTester(String input) {
        path = input;
        fullMatch = !path.endsWith(STUFFIX_FLAG);

        if (fullMatch || path.length() < 2) {
            matchString = path;
        } else {
            matchString = path.substring(0, path.length() - 1);
        }
        matchLength = matchString.length();
    }

    public boolean test(String input) {
        return fullMatch || input.length() == matchLength ? matchString.equals(input)
                : input.startsWith(path);
    }

    public String toPathMatch() {
        return fullMatch ? path : (path + "**");
    }

    public static boolean isPattern(String input) {
        return input.indexOf('*') != -1 && input.indexOf("?") != -1;
    }
}

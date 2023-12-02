package org.vxwo.springboot.experience.web.matcher;

import lombok.Getter;

/**
 * Simple path tester
 *
 * @author vxwo-team
 */

public class PathTester {
    private final static String PREFIX_FLAG = "/";

    @Getter
    private final String path;
    private final boolean fullMatch;

    public PathTester(String input) {
        path = input;
        fullMatch = !path.endsWith(PREFIX_FLAG);
    }

    public boolean test(String input) {
        return fullMatch ? path.equals(input) : input.startsWith(path);
    }

    public String toPathMatch() {
        return fullMatch ? path : (path + "**");
    }
}

package org.vxwo.springboot.experience.web.matcher;

/**
 * Simple path matcher, contains extra attribute
 *
 * @author vxwo-team
 */

public class ExtraPathMatcher<T> extends PathMatcher {
    private T extra;

    public ExtraPathMatcher(String path, T extra) {
        super(path);
        this.extra = extra;
    }

    public T getExtra() {
        return extra;
    }
}

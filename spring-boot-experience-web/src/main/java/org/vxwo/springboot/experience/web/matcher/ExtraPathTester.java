package org.vxwo.springboot.experience.web.matcher;

/**
 * Simple path tester, contains extra attribute
 *
 * @author vxwo-team
 */

public class ExtraPathTester<T> extends PathTester {
    private T extra;

    public ExtraPathTester(String path, T extra) {
        super(path);
        this.extra = extra;
    }

    public T getExtra() {
        return extra;
    }
}

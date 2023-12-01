package org.vxwo.springboot.experience.web.matcher;

import org.springframework.util.StringUtils;

/**
 * Simple path tester, contains extra attribute
 *
 * @author vxwo-team
 */

public class TagPathTester<T> extends PathTester {
    private String tag;
    private T extra;

    public TagPathTester(String tag, String path, T extra) {
        super(path);
        this.extra = extra;
        this.tag = StringUtils.hasText(tag) ? tag : "default";
    }

    public String getTag() {
        return tag;
    }

    public T getExtra() {
        return extra;
    }
}

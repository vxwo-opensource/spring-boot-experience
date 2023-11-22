package org.vxwo.springboot.experience.web.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class SplitUtil {

    /**
     * Shrink list, remove blank element
     *
     * @param value  the list for shrink
     * @return  the list by shrinked
     */
    public static List<String> shrinkList(List<String> value) {
        if (value == null) {
            return new ArrayList<>();
        }

        return value.stream().map(o -> o.trim()).filter(o -> !o.isEmpty())
                .collect(Collectors.toList());
    }

    /**
     * Splits the string around matches of the given regular expression.
     *
     * @param value  the string for split for
     * @param regex  the delimiting regular expression
     * @return  the list of strings computed by splitting
     */
    public static List<String> splitToList(String value, String regex) {
        return shrinkList(Arrays.asList(value.split(regex)));
    }
}

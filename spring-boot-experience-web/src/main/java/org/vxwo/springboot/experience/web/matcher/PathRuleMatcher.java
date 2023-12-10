package org.vxwo.springboot.experience.web.matcher;

import java.util.List;

/**
 * @author vxwo-team
 */

public interface PathRuleMatcher {
    /**
     * Get Ant style path matchs
     *
     * @param tag  The path Tag
     * @return The path matchs for include
     */
    List<String> getPathMatchs(String tag);

    /**
     * Get Ant style path matchs for exclude
     *
     * @param tag  The path Tag
     * @return  The path matchs for exclude
     */
    List<String> getExcludePathMatchs(String tag);
}

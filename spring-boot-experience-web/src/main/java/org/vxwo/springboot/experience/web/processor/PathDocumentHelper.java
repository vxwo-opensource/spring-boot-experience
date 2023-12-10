package org.vxwo.springboot.experience.web.processor;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.vxwo.springboot.experience.web.filter.ApiKeyAuthorizationFilter;
import org.vxwo.springboot.experience.web.filter.BearerAuthorizationFilter;

/**
 * Simple path document helper
 *
 * @author vxwo-team
 */

public class PathDocumentHelper {

    @Autowired(required = false)
    private ApiKeyAuthorizationFilter apikeyAuthorization;

    @Autowired(required = false)
    private BearerAuthorizationFilter bearerAuthorization;

    /**
     * Get Ant style path matchs for ApiKey Authorization
     *
     * @param tag  The path tag
     * @return  The path matchs for include
     */
    public List<String> getApiKeyPathMatchs(String tag) {
        List<String> pathMatches = new ArrayList<>();
        if (apikeyAuthorization != null) {
            pathMatches.addAll(apikeyAuthorization.getPathRuleMatcher().getPathMatchs(tag));
        }
        return pathMatches;
    }

    /**
     * Get Ant style path matchs for Bearer Authorization
     *
     * @param tag  The path tag
     * @return  The path matchs for include
     */
    public List<String> getBearerPathMatchs(String tag) {
        List<String> pathMatches = new ArrayList<>();
        if (apikeyAuthorization != null) {
            pathMatches.addAll(bearerAuthorization.getPathRuleMatcher().getPathMatchs(tag));
        }
        return pathMatches;
    }

    /**
     * Get Ant style exclude path matchs for Bearer Authorization
     *
     * @param tag  The path tag
     * @return  The path matchs for exclude
     */
    public List<String> getBearerExcludePathMatchs(String tag) {
        List<String> pathMatches = new ArrayList<>();
        if (apikeyAuthorization != null) {
            pathMatches.addAll(bearerAuthorization.getPathRuleMatcher().getExcludePathMatchs(tag));
        }
        return pathMatches;
    }
}

package org.vxwo.springboot.experience.web.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.vxwo.springboot.experience.web.filter.ApiKeyAuthorizationFilter;
import org.vxwo.springboot.experience.web.filter.BearerAuthorizationFilter;
import org.vxwo.springboot.experience.web.matcher.TagPathTester;

/**
 * Simple path document helper
 *
 * @author vxwo-team
 */

public class PathDocumentHelper {

    @Autowired
    private PathProcessor pathProcessor;

    @Autowired(required = false)
    private ApiKeyAuthorizationFilter apikeyAuthorization;

    @Autowired(required = false)
    private BearerAuthorizationFilter bearerAuthorization;

    /**
     * Convert relative path matches to absolute path matches
     *
     * @param input  The path matches
     * @return  The absolute path matchs
     */
    public List<String> absPathMatches(List<String> input) {
        return input.stream().map(o -> pathProcessor.createAbsoluteURI(o))
                .collect(Collectors.toList());
    }

    /**
     * Get Ant style absolute path matchs for ApiKey Authorization
     *
     * @param tag  The path tag, null is `default`
     * @return  The path matchs for include
     */
    public List<String> getApiKeyPathMatchs(String tag) {
        List<String> pathMatches = new ArrayList<>();
        if (apikeyAuthorization != null) {
            pathMatches.addAll(absPathMatches(apikeyAuthorization.getPathRuleMatcher()
                    .getPathMatchs(TagPathTester.fixTag(tag))));
        }
        return pathMatches;
    }

    /**
     * Get Ant style absolute path matchs for Bearer Authorization
     *
     * @param tag  The path tag, null is `default`
     * @return  The path matchs for include
     */
    public List<String> getBearerPathMatchs(String tag) {
        List<String> pathMatches = new ArrayList<>();
        if (apikeyAuthorization != null) {
            pathMatches.addAll(absPathMatches(bearerAuthorization.getPathRuleMatcher()
                    .getPathMatchs(TagPathTester.fixTag(tag))));
        }
        return pathMatches;
    }

    /**
     * Get Ant style absolute exclude path matchs for Bearer Authorization
     *
     * @param tag  The path tag, null is `default`
     * @return  The path matchs for exclude
     */
    public List<String> getBearerExcludePathMatchs(String tag) {
        List<String> pathMatches = new ArrayList<>();
        if (apikeyAuthorization != null) {
            pathMatches.addAll(absPathMatches(bearerAuthorization.getPathRuleMatcher()
                    .getExcludePathMatchs(TagPathTester.fixTag(tag))));
        }
        return pathMatches;
    }
}

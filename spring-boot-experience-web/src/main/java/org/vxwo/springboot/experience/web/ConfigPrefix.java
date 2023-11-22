package org.vxwo.springboot.experience.web;

/**
 * @author vxwo-team
 */

public final class ConfigPrefix {
    private final static String ATTRIBUTE_PREFIX = "sbexp.web";

    public final static String CORS = ATTRIBUTE_PREFIX + ".cors";

    public final static String REQUEST_LOGGING = ATTRIBUTE_PREFIX + ".logging";

    public final static String AUTHORIZATION_API_KEY = ATTRIBUTE_PREFIX + ".authorization.api-key";

    public final static String AUTHORIZATION_BEARER = ATTRIBUTE_PREFIX + ".authorization.bearer";

    public final static String FREQUENCY_CONTROL = ATTRIBUTE_PREFIX + ".frequency-control";
}

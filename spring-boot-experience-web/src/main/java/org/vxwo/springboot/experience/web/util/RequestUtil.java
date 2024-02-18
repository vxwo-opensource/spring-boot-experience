package org.vxwo.springboot.experience.web.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author vxwo-team
 */

public final class RequestUtil {
    private final static String LOCALVALUES_ATTRIBUTE =
            "SBEXP:" + UUID.randomUUID().toString() + ":LocalValues";

    /**
     * Get current HttpServletRequest
     *
     * @return  instance of HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        HttpServletRequest request = null;
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            request = ((ServletRequestAttributes) requestAttributes).getRequest();
        }
        if (request == null) {
            throw new UnsupportedOperationException();
        }
        return request;
    }

    /**
     * Get local values Map in current HttpServletRequest
     *
     * @return  local values Map
     */
    public static Map<String, Object> getLocalValues() {
        HttpServletRequest request = getRequest();
        @SuppressWarnings("unchecked")
        Map<String, Object> values =
                (Map<String, Object>) request.getAttribute(LOCALVALUES_ATTRIBUTE);
        if (values == null) {
            values = new HashMap<>(10);
            request.setAttribute(LOCALVALUES_ATTRIBUTE, values);
        }

        return values;
    }

    /**
     * Get local value in current HttpServletRequest
     *
     * @param key  the Key
     * @return  the Value
     */
    public static Object getLocalValue(String key) {
        return getLocalValues().get(key);
    }

    /**
     *  Put local value to current HttpServletRequest
     *
     * @param key  the Key
     * @param value  the Value
     */
    public static void putLocalValue(String key, Object value) {
        getLocalValues().put(key, value);
    }
}

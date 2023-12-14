package org.vxwo.springboot.experience.web.filter;

import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.vxwo.springboot.experience.web.matcher.TagPathTester;

/**
 * @author vxwo-team
 */

class AuthorizationHelper {
    private final static String ATTRIBUTE_NAME =
            "SBEXP:" + UUID.randomUUID().toString() + ":AuthorizationActived";

    public static boolean isProcessed(HttpServletRequest request) {
        return request.getAttribute(ATTRIBUTE_NAME) != null;
    }

    public static void markProcessed(HttpServletRequest request, TagPathTester<?> tester) {
        request.setAttribute(ATTRIBUTE_NAME, tester);
    }

    public static TagPathTester<?> getProcessTester(HttpServletRequest request) {
        return (TagPathTester<?>) request.getAttribute(ATTRIBUTE_NAME);
    }
}

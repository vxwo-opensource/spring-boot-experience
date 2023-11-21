package org.vxwo.springboot.experience.web.handler;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The interface for authorization failure handle
 *
 * @author vxwo-team
 */

public interface AuthorizationFailureHandler {

    /**
     * Handle the authorization failure
     *
     * @param request  the request wrap
     * @param response  the response wrap
     * @param matchPath  the matched path rule
     * @param message  the failure message
     * @throws ServletException  if the request cannot be handled
     * @throws IOException  if IO error occurs
     */
    void handleAuthorizationFailure(HttpServletRequest request, HttpServletResponse response,
            String matchPath, String message) throws ServletException, IOException;
};

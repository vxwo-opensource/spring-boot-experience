package org.vxwo.springboot.experience.web.handler;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * The interface for bearer authorization handle
 *
 * @author vxwo-team
 */

public interface BearerAuthorizationHandler {

    /**
     * Process the bearer authorization
     *
     * @param request  the request wrap
     * @param response  the response wrap
     * @param tag  the matched path tag
     * @param matchPath  the matched path rule
     * @param bearerToken  the bearer token
     * @return  process status
     * @throws ServletException  if the request cannot be handled
     * @throws IOException  if IO error occurs
     */
    boolean processBearerAuthorization(HttpServletRequest request, HttpServletResponse response,
            String tag, String matchPath, String bearerToken) throws ServletException, IOException;
};

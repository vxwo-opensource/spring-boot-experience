package org.vxwo.springboot.experience.web.handler;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The interface for bearer authorization handle
 *
 * @author vxwo-team
 */

public interface BearerAuthorizationHandler {

    /**
     * Process the bearer token
     *
     * @param request  the request wrap
     * @param response  the response wrap
     * @param matchPath  the matched path rule
     * @param bearerToken  the bearer token
     * @return  process status
     * @throws ServletException  if the request cannot be handled
     * @throws IOException  if IO error occurs
     */
    boolean processBearerToken(HttpServletRequest request, HttpServletResponse response,
            String matchPath, String bearerToken) throws ServletException, IOException;
};

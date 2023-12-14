package org.vxwo.springboot.experience.web.handler;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The interface for manual authorization handle
 *
 * @author vxwo-team
 */

public interface ManualAuthorizationHandler {

    /**
     * Process the manual authorization
     *
     * @param request  the request wrap
     * @param response  the response wrap
     * @param tag  the matched path tag
     * @param matchPath  the matched path rule
     * @return  process status
     * @throws ServletException  if the request cannot be handled
     * @throws IOException  if IO error occurs
     */
    boolean processManualAuthorization(HttpServletRequest request, HttpServletResponse response,
            String tag, String matchPath) throws ServletException, IOException;
};

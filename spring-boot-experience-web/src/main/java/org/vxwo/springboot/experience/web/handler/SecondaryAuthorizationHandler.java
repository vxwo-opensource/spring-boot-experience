package org.vxwo.springboot.experience.web.handler;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * The interface for secondary authorization handle
 *
 * @author vxwo-team
 */

public interface SecondaryAuthorizationHandler {

    /**
     * Process the secondary authorization
     *
     * @param request  the request wrap
     * @param response  the response wrap
     * @return  process status
     * @throws ServletException  if the request cannot be handled
     * @throws IOException  if IO error occurs
     */
    boolean processSecondaryAuthorization(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;
};

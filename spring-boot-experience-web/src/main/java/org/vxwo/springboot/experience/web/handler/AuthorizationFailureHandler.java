package org.vxwo.springboot.experience.web.handler;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author vxwo-team
 */

public interface AuthorizationFailureHandler {

    /**
     * Handle the authorization failure
     *
     * @param request
     * @param response
     * @param path
     * @param detail
     * @throws ServletException
     * @throws IOException
     */
    void handleAuthorizationFailure(HttpServletRequest request, HttpServletResponse response,
            String path, String detail) throws ServletException, IOException;
};

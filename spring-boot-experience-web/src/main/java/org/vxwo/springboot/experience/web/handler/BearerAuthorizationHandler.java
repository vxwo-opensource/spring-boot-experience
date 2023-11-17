package org.vxwo.springboot.experience.web.handler;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author vxwo-team
 */

public interface BearerAuthorizationHandler {

    /**
     * Process the bearer token
     *
     * @param request
     * @param response
     * @param matchPath
     * @param bearerToken
     * @return
     * @throws ServletException
     * @throws IOException
     */
    boolean processBearerToken(HttpServletRequest request, HttpServletResponse response,
            String matchPath, String bearerToken) throws ServletException, IOException;
};

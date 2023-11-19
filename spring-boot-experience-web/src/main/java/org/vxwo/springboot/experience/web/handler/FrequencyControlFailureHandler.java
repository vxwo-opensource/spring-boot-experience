package org.vxwo.springboot.experience.web.handler;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author vxwo-team
 */

public interface FrequencyControlFailureHandler {

    /**
     * Handle the frequency control failure
     *
     * @param request
     * @param response
     * @param method
     * @param matchPath
     * @param message
     * @throws ServletException
     * @throws IOException
     */
    void handleFrequencyControlFailure(HttpServletRequest request, HttpServletResponse response,
            String method, String matchPath, String message) throws ServletException, IOException;
};

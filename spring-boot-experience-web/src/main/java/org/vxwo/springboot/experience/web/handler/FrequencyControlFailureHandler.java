package org.vxwo.springboot.experience.web.handler;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The interface for frequency control failure handle
 *
 * @author vxwo-team
 */

public interface FrequencyControlFailureHandler {

    /**
     * Handle the frequency control concurrency failure
     *
     * @param request  the request wrap
     * @param response  the response wrap
     * @param method  the http method
     * @param matchPath  the matched path rule
     * @throws ServletException  if the request cannot be handled
     * @throws IOException  if IO error occurs
     */
    void handleFrequencyControlConcurrencyFailure(HttpServletRequest request,
            HttpServletResponse response, String method, String matchPath)
            throws ServletException, IOException;

    /**
     * Handle the frequency control fixed interval failure
     *
     * @param request  the request wrap
     * @param response  the response wrap
     * @param method  the http method
     * @param tag  the matched path tag
     * @param matchPath  the matched path rule
     * @throws ServletException  if the request cannot be handled
     * @throws IOException  if IO error occurs
     */
    void handleFrequencyControlFixedIntervalFailure(HttpServletRequest request,
            HttpServletResponse response, String method, String tag, String matchPath)
            throws ServletException, IOException;
};

package org.vxwo.springboot.experience.web.handler;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * The interface for frequency control handle
 *
 * @author vxwo-team
 */

public interface FrequencyControlHandler {
    /**
     * enter concurrency session
     *
     * @param request  the request wrap
     * @param response  the response wrap
     * @param keyPrefix  the key prefix for identify the function
     * @param duration  the duration time
     * @param localContext  the context for leave
     * @return  success if entered
     * @throws ServletException  if the request cannot be handled
     * @throws IOException  if IO error occurs
     */
    boolean enterConcurrency(HttpServletRequest request, HttpServletResponse response,
            String keyPrefix, Duration duration, Map<String, Object> localContext)
            throws ServletException, IOException;

    /**
     * leave concurrency session
     *
     * @param request  the request wrap
     * @param response  the response wrap
     * @param localContext  the context from enter
     * @throws ServletException  if the request cannot be handled
     * @throws IOException  if IO error occurs
     */
    void leaveConcurrency(HttpServletRequest request, HttpServletResponse response,
            Map<String, Object> localContext) throws ServletException, IOException;

    /**
     * obtain fixed interval session
     *
     * @param request  the request wrap
     * @param response  the response wrap
     * @param keyPrefix  the key prefix for identify the function
     * @param duration  the duration time
     * @return  success if obtaind
     * @throws ServletException  if the request cannot be handled
     * @throws IOException  if IO error occurs
     */
    boolean obtainFixedInterval(HttpServletRequest request, HttpServletResponse response,
            String keyPrefix, Duration duration) throws ServletException, IOException;

};

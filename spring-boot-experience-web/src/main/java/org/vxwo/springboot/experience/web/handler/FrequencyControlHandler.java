package org.vxwo.springboot.experience.web.handler;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author vxwo-team
 */

public interface FrequencyControlHandler {
    /**
     * enter concurrency
     *
     * @param request
     * @param response
     * @param keyPrefix
     * @param duration
     * @param localContext
     * @return
     * @throws ServletException
     * @throws IOException
     */
    boolean enterConcurrency(HttpServletRequest request, HttpServletResponse response,
            String keyPrefix, Duration duration, Map<String, Object> localContext)
            throws ServletException, IOException;

    /**
     * leave concurrency
     *
     * @param request
     * @param response
     * @param localContext
     * @throws ServletException
     * @throws IOException
     */
    void leaveConcurrency(HttpServletRequest request, HttpServletResponse response,
            Map<String, Object> localContext) throws ServletException, IOException;

    /**
     * obtain fixed interval
     *
     * @param request
     * @param response
     * @param keyPrefix
     * @param duration
     * @return
     * @throws ServletException
     * @throws IOException
     */
    boolean obtainFixedInterval(HttpServletRequest request, HttpServletResponse response,
            String keyPrefix, Duration duration) throws ServletException, IOException;

};

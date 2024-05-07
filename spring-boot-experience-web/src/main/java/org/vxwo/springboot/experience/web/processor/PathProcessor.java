package org.vxwo.springboot.experience.web.processor;

import java.net.URI;
import java.util.UUID;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * Simple path processor
 *
 * @author vxwo-team
 */

@Slf4j
public class PathProcessor {
    private final static String FAKE_HOST = "http://localhost";
    private final static int FAKE_LENGTH = FAKE_HOST.length();

    private final static String ATTRIBUTE_NAME =
            "SBEXP:" + UUID.randomUUID().toString() + ":RelativeURI";

    private String servletContextPath;
    private int servletContextPathLength = 0;

    @Autowired
    private void setServletContextPath(
            @Value(value = "${server.servlet.context-path:/}") String contextPath) {
        servletContextPath = contextPath.replaceAll("\\/$", "");
        servletContextPathLength = servletContextPath.length();

        if (log.isInfoEnabled()) {
            log.info("Path processor actived on: " + servletContextPath + "/");
        }
    }

    /**
     * Generateion absolute URI from relative URI
     *
     * @param  relativeURI  the relative URI
     * @return  the absolute URI
     */
    @SuppressWarnings("PMD")
    public String createAbsoluteURI(String relativeURI) {
        return servletContextPath + relativeURI;
    }

    /**
     * Get relative URI from request
     *
     * @param  request  the request wrap
     * @return  the relative URI
     */
    @SuppressWarnings("PMD")
    public String getRelativeURI(HttpServletRequest request) {
        String targetURI = (String) request.getAttribute(ATTRIBUTE_NAME);
        if (StringUtils.hasText(targetURI)) {
            return targetURI;
        }

        targetURI = request.getRequestURI();
        try {
            targetURI =
                    URI.create(FAKE_HOST + targetURI).normalize().toString().substring(FAKE_LENGTH);
        } catch (Exception ex) {
        }

        if (targetURI.length() > servletContextPathLength) {
            targetURI = targetURI.substring(servletContextPathLength);
        }

        request.setAttribute(ATTRIBUTE_NAME, targetURI);
        return targetURI;
    }

}

package org.vxwo.springboot.experience.web.processor;

import java.net.URI;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

/**
 * Simple path processor
 *
 * @author vxwo-team
 */

@Slf4j
@Component
public class PathProcessor {
    private final static String FAKE_HOST = "http://localhost";
    private final static int FAKE_LENGTH = FAKE_HOST.length();

    private String servletContextPath;
    private int servletContextPathLength = 0;

    @Autowired
    private void setServletContextPath(
            @Value(value = "${server.servlet.context-path:/}") String contextPath) {
        servletContextPath = contextPath.replaceAll("\\/$", "");
        servletContextPathLength = servletContextPath.length();

        if (log.isInfoEnabled()) {
            log.info("PathProcessor actived on: " + servletContextPath + "/");
        }
    }

    /**
     * Create absolute URL
     *
     * @param Relative Path
     * @return Absolute URL
     */

    @SuppressWarnings("PMD")
    public String createAbsoluteURL(String relativeURI) {
        return servletContextPath + relativeURI;
    }

    /**
     * Get current relative URI
     *
     * @param Request
     * @return Relative URI
     */

    @SuppressWarnings("PMD")
    public String getRelativeURI(HttpServletRequest request) {
        String targetURI = request.getRequestURI();

        try {
            targetURI =
                    URI.create(FAKE_HOST + targetURI).normalize().toString().substring(FAKE_LENGTH);
        } catch (Exception ex) {
        }

        if (targetURI.length() > servletContextPathLength) {
            targetURI = targetURI.substring(servletContextPathLength);
        }

        return targetURI;
    }

}

package org.vxwo.springboot.experience.web.handler;

import java.io.IOException;
import java.util.Map;
import org.vxwo.springboot.experience.web.entity.RequestLoggingEntity;

/**
 * @author vxwo-team
 */

public interface RequestLoggingHandler {

    /**
    * Convert to String
    *
    * @param value
    * @return
    * @throws IOException
    */
    String convertToString(Object value) throws IOException;

    /**
     * Convert Object to Map
     *
     * @param value
     * @return
     * @throws IOException
     */
    Map<String, Object> convertToMap(Object value) throws IOException;

    /**
     * Handle request logging
     *
     * @param entity
     * @throws IOException
     */
    void handleRequestLogging(RequestLoggingEntity entity) throws IOException;
};

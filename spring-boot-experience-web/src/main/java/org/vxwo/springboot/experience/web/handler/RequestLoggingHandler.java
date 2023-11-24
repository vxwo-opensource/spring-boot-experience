package org.vxwo.springboot.experience.web.handler;

import java.io.IOException;
import java.util.Map;
import org.vxwo.springboot.experience.web.entity.RequestLoggingEntity;

/**
 * The interface for request loging handle
 *
 * @author vxwo-team
 */

public interface RequestLoggingHandler {

    /**
    * Convert object to JSON String
    *
    * @param value  the object
    * @return  the JSON string
    * @throws IOException  if IO error occurs
    */
    String convertToString(Object value) throws IOException;

    /**
     * Convert object to Map
     *
     * @param value  the object
     * @return  the Map
     * @throws IOException  if IO error occurs
     */
    Map<String, Object> convertToMap(Object value) throws IOException;

    /**
     * Publish request logging to repository
     *
     * @param entity  the logging entity
     * @throws IOException  if IO error occurs
     */
    void publishRequestLogging(RequestLoggingEntity entity) throws IOException;
};

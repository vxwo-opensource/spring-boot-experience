package org.vxwo.springboot.experience.util.json;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author vxwo-team
 */

public interface ObjectMapperRender {

    /**
     * Render the ObjectMapper
     *
     * @param objectMapper  instance of ObjectMapper
     */
    void render(ObjectMapper objectMapper);
}

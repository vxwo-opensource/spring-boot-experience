package org.vxwo.springboot.experience.util.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

/**
 * @author vxwo-team
 */

public class ObjectMapperBuilder {
    private ObjectMapper objectMapper;

    private ObjectMapperBuilder() {
        objectMapper = new ObjectMapper();
    }

    public ObjectMapperBuilder useDefault() {
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new ParameterNamesModule());

        objectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);

        return this;
    }

    public ObjectMapperBuilder useRender(ObjectMapperRender render) {
        render.render(objectMapper);

        return this;
    }

    public ObjectMapper build() {
        return objectMapper;
    }

    public static ObjectMapperBuilder builder() {
        return new ObjectMapperBuilder();
    }
}

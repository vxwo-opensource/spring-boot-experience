package org.vxwo.springboot.experience.mybatis.handlers;

import com.fasterxml.jackson.databind.JavaType;

/**
 * @author vxwo-team
 */

public class SimpleJsonTypeHandler<T> extends BaseJsonTypeHandler<T> {
    private final Class<?> type;

    public SimpleJsonTypeHandler(Class<?> type) {
        this.type = type;
    }

    @Override
    protected JavaType getGenericValueType() {
        return OBJECT_MAPPER.constructType(type);
    }
}

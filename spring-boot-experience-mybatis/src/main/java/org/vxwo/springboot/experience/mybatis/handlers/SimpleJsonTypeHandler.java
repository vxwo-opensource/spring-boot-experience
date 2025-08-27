package org.vxwo.springboot.experience.mybatis.handlers;

import org.apache.ibatis.type.*;
import com.fasterxml.jackson.databind.JavaType;

/**
 * @author vxwo-team
 */

@MappedTypes(Object.class)
@MappedJdbcTypes({JdbcType.VARCHAR, JdbcType.CHAR, JdbcType.LONGVARCHAR})
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

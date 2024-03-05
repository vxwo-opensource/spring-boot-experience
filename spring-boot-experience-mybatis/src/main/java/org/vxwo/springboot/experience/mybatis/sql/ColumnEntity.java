package org.vxwo.springboot.experience.mybatis.sql;

import java.lang.reflect.Method;
import org.apache.ibatis.type.TypeHandler;

/**
 * @author vxwo-team
 */

@SuppressWarnings("rawtypes")
public final class ColumnEntity {
    private final String name;
    private final String fieldName;
    private final Method fieldGetter;
    private final Class<? extends TypeHandler> typeHandler;

    private ColumnEntity(String name, String fieldName, Method fieldGetter,
            Class<? extends TypeHandler> typeHandler) {
        this.name = name;
        this.fieldName = fieldName;
        this.fieldGetter = fieldGetter;
        this.typeHandler = typeHandler;
    }

    public String getName() {
        return name;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getFieldValue(Object object) {
        try {
            return fieldGetter.invoke(object);
        } catch (Exception ex) {
            return null;
        }
    }

    public String getFieldTypeHandlerName() {
        return typeHandler == null ? null : typeHandler.getName();
    }

    public static ColumnEntity of(String columnName, String fieldName, Method fieldGetter,
            Class<? extends TypeHandler> typeHandler) {
        return new ColumnEntity(columnName, fieldName, fieldGetter, typeHandler);
    }
}

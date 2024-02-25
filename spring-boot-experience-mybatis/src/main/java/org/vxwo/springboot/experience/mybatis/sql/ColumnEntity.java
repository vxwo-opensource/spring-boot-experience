package org.vxwo.springboot.experience.mybatis.sql;

import java.lang.reflect.Method;

/**
 * @author vxwo-team
 */

public final class ColumnEntity {
    private final String name;
    private final String fieldName;
    private final Method fieldGetter;

    private ColumnEntity(String name, String fieldName, Method fieldGetter) {
        this.name = name;
        this.fieldName = fieldName;
        this.fieldGetter = fieldGetter;
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

    public static ColumnEntity of(String columnName, String fieldName, Method fieldGetter) {
        return new ColumnEntity(columnName, fieldName, fieldGetter);
    }
}

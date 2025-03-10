package org.vxwo.springboot.experience.mybatis.annotations;

import java.lang.annotation.*;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.UnknownTypeHandler;

/**
 * This annotation can be used to tell GeneralSqlProvider the field attributes
 *
 * @author vxwo-team
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@SuppressWarnings("rawtypes")
public @interface GeneralField {
    /**
     * Indicates whether this field is allowed to be used in add operations
     *
     * @return true if the field is allowed in insertion operations,
     *         defaults to false
     */
    boolean allowAdd() default false;

    /**
     * Specifies the TypeHandler class for column type conversion
     *
     * @return the TypeHandler class to be used, defaults to UnknownTypeHandler.class
     *         indicating no specific handler is specified
     */
    Class<? extends TypeHandler> typeHandler() default UnknownTypeHandler.class;
}

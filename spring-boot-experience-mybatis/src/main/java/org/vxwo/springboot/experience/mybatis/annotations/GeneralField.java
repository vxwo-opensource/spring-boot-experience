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
     * Returns the column TypeHandler
     *
     * return  the column TypeHandler
     */
    Class<? extends TypeHandler> typeHandler() default UnknownTypeHandler.class;
}

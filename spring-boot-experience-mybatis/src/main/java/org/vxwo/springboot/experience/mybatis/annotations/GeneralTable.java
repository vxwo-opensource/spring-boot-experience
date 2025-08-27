package org.vxwo.springboot.experience.mybatis.annotations;

import java.lang.annotation.*;

/**
 * This annotation can be used to tell GeneralSqlProvider the table name
 *
 * @author vxwo-team
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface GeneralTable {
    /**
     * Returns the table name
     *
     * @return  the table name
     */
    String value() default "";
}

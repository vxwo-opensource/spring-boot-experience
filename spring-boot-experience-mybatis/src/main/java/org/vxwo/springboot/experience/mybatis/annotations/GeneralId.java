package org.vxwo.springboot.experience.mybatis.annotations;

import java.lang.annotation.*;

/**
 * This annotation can be used to tell GeneralSqlProvider the `id` column
 *
 * @author vxwo-team
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface GeneralId {
}

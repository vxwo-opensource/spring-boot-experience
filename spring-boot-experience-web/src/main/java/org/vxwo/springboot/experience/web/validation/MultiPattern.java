package org.vxwo.springboot.experience.web.validation;

import java.lang.annotation.*;
import javax.validation.*;

/**
 * @author vxwo-team
 */

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = MultiPatternValidator.class)
public @interface MultiPattern {
    String value();

    char splitChar() default ',';

    String reserveValue() default "";

    boolean allowEmpty() default true;

    String message() default "It should be unique by pattern: {value}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

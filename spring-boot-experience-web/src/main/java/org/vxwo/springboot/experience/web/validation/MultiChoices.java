package org.vxwo.springboot.experience.web.validation;

import java.lang.annotation.*;
import javax.validation.*;

/**
 * @author vxwo-team
 */

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = MultiChoicesValidator.class)
public @interface MultiChoices {
    String[] values();

    char splitChar() default ',';

    boolean allowEmpty() default true;

    String message() default "It should be unique of: {values}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

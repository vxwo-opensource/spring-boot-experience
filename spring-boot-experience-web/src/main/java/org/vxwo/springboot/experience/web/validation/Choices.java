package org.vxwo.springboot.experience.web.validation;

import java.lang.annotation.*;
import jakarta.validation.*;

/**
 * @author vxwo-team
 */

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ChoicesValidator.class)
public @interface Choices {
    String[] values();

    boolean allowEmpty() default true;

    String message() default "It should be one of: {values}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

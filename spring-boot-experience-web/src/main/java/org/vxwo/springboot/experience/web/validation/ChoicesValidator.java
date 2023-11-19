package org.vxwo.springboot.experience.web.validation;

import java.util.*;
import javax.validation.*;

/**
 * @author vxwo-team
 */

public class ChoicesValidator implements ConstraintValidator<Choices, String> {

    private boolean alloweEmpty;
    private List<String> allowValues;

    @Override
    public void initialize(Choices annotation) {
        allowValues = Arrays.asList(annotation.values());
        alloweEmpty = annotation.allowEmpty();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return alloweEmpty;
        }

        return allowValues.contains(value);
    }

}

package org.vxwo.springboot.experience.web.validation;

import java.util.*;
import javax.validation.*;

/**
 * @author vxwo-team
 */

public class MultiChoicesValidator implements ConstraintValidator<MultiChoices, String> {

    private List<String> allowValues;

    private String split;
    private boolean alloweEmpty;

    @Override
    public void initialize(MultiChoices annotation) {
        allowValues = Arrays.asList(annotation.values());

        split = String.valueOf(annotation.splitChar());
        alloweEmpty = annotation.allowEmpty();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return alloweEmpty;
        }

        boolean passed = true;
        List<String> unique = new ArrayList<>();

        for (String s : value.split(split)) {
            passed = allowValues.contains(s) && !unique.contains(s);
            if (!passed) {
                break;
            }

            unique.add(s);
        }

        return passed;
    }
}

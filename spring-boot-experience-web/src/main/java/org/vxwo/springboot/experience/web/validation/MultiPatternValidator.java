package org.vxwo.springboot.experience.web.validation;

import java.util.*;
import java.util.regex.Pattern;
import javax.validation.*;

/**
 * @author vxwo-team
 */

public class MultiPatternValidator implements ConstraintValidator<MultiPattern, String> {

    private Pattern pattern;
    private Pattern reservePattern;

    private String splitChar;
    private boolean alloweEmpty;

    @Override
    public void initialize(MultiPattern annotation) {
        pattern = Pattern.compile(annotation.value());

        if (annotation.reserveValue() != null && !annotation.reserveValue().isEmpty()) {
            reservePattern = Pattern.compile(annotation.reserveValue());
        }

        splitChar = String.valueOf(annotation.splitChar());
        alloweEmpty = annotation.allowEmpty();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return alloweEmpty;
        }

        boolean passed = true;
        List<String> unique = new ArrayList<>();

        for (String s : value.split(splitChar)) {
            passed = pattern.matcher(s).matches() && !unique.contains(s);
            if (!passed) {
                break;
            }

            unique.add(s);
        }

        if (!passed && reservePattern != null) {
            passed = reservePattern.matcher(value).matches();
        }

        return passed;
    }
}

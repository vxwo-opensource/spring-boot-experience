package org.vxwo.springboot.experience.web.testor;

import org.vxwo.springboot.experience.web.validation.*;
import lombok.Data;

public class CustomRequestBody {
    @Data
    public static class ChoicesBody {
        @Choices(values = {"a", "b"})
        private String v;
    }

    @Data
    public static class MultiChoicesBody {
        @MultiChoices(values = {"a", "b", "c"})
        private String v;
    }

    @Data
    public static class MultiPatternBody {
        @MultiPattern(value = "^[0-9]+$", reserveValue = "(A|B)")
        private String v;
    }
}

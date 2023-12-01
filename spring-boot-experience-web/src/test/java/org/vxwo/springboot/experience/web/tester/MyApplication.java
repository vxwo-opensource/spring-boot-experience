package org.vxwo.springboot.experience.web.tester;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import static org.vxwo.springboot.experience.web.tester.CustomRequestBody.*;

@RestController
@SpringBootApplication
@Validated
public class MyApplication {

    @GetMapping("/test-api-key")
    public String doTestApiKey() {
        return ReturnCode.SUCCESS;
    }

    @GetMapping("/test-bearer/include-path")
    public String doTestBearerInclude() {
        return ReturnCode.SUCCESS;
    }

    @GetMapping("/test-bearer/exclude-path")
    public String doTestBearerExclude() {
        return ReturnCode.SUCCESS;
    }

    @GetMapping("/test-bearer/optional-path")
    public String doTestBearerOptional(
            @RequestAttribute(value = ReturnCode.LOGINED, required = false) String logined) {
        return StringUtils.hasText(logined) ? ReturnCode.LOGINED : ReturnCode.SUCCESS;
    }

    @GetMapping("/test-frequency/concurrency")
    public String doTestFrequencyConcurrency() {
        return ReturnCode.SUCCESS;
    }

    @GetMapping("/test-frequency/fixed-interval")
    public String doTestFrequencyFixedInterval() {
        return ReturnCode.SUCCESS;
    }

    @PostMapping("/test-validation/choices")
    public String doTestValidationChoices(@Validated @RequestBody ChoicesBody body) {
        return ReturnCode.SUCCESS;
    }

    @PostMapping("/test-validation/multi-choices")
    public String doTestValidationMultiChoices(@Validated @RequestBody MultiChoicesBody body) {
        return ReturnCode.SUCCESS;
    }

    @PostMapping("/test-validation/multi-pattern")
    public String doTestValidationMultiPattern(@Validated @RequestBody MultiPatternBody body) {
        return ReturnCode.SUCCESS;
    }

}

package org.vxwo.springboot.experience.web.tester;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.vxwo.springboot.experience.web.processor.PathDocumentHelper;
import static org.vxwo.springboot.experience.web.tester.CustomRequestBody.*;
import java.util.List;
import javax.validation.constraints.NotBlank;

@RestController
@SpringBootApplication
@Validated
public class MyApplication {

    @Autowired
    private PathDocumentHelper documentHelper;

    private AntPathMatcher pathMatcher = new AntPathMatcher();

    @GetMapping("/api-key")
    public String doTestApiKey() {
        return ReturnCode.SUCCESS;
    }

    @GetMapping("/bearer/include-path")
    public String doTestBearerInclude() {
        return ReturnCode.SUCCESS;
    }

    @GetMapping("/bearer/exclude-path")
    public String doTestBearerExclude() {
        return ReturnCode.SUCCESS;
    }

    @GetMapping("/bearer/optional-path")
    public String doTestBearerOptional(
            @RequestAttribute(value = ReturnCode.LOGINED, required = false) String logined) {
        return StringUtils.hasText(logined) ? ReturnCode.LOGINED : ReturnCode.SUCCESS;
    }

    @GetMapping("/manual/include-path")
    public String doTestManualInclude() {
        return ReturnCode.SUCCESS;
    }

    @GetMapping("/manual/exclude-path")
    public String doTestManualExclude() {
        return ReturnCode.SUCCESS;
    }

    @GetMapping("/manual/optional-path")
    public String doTestManualOptional(
            @RequestAttribute(value = ReturnCode.LOGINED, required = false) String logined) {
        return StringUtils.hasText(logined) ? ReturnCode.LOGINED : ReturnCode.SUCCESS;
    }

    @GetMapping("/frequency/concurrency")
    public String doTestFrequencyConcurrency() {
        return ReturnCode.SUCCESS;
    }

    @GetMapping("/frequency/fixed-interval")
    public String doTestFrequencyFixedInterval() {
        return ReturnCode.SUCCESS;
    }

    @PostMapping("/validation/choices")
    public String doTestValidationChoices(@Validated @RequestBody ChoicesBody body) {
        return ReturnCode.SUCCESS;
    }

    @PostMapping("/validation/multi-choices")
    public String doTestValidationMultiChoices(@Validated @RequestBody MultiChoicesBody body) {
        return ReturnCode.SUCCESS;
    }

    @PostMapping("/validation/multi-pattern")
    public String doTestValidationMultiPattern(@Validated @RequestBody MultiPatternBody body) {
        return ReturnCode.SUCCESS;
    }

    @GetMapping("/document-helper/apikey")
    public String doTestDocumentHelperForApiKey(@Validated @NotBlank String url) {
        List<String> pathMatches = documentHelper.getApiKeyPathMatchs("test");
        long matchCount = pathMatches.stream().filter(o -> pathMatcher.match(o, url)).count();
        return matchCount < 1 ? ReturnCode.FAILED : ReturnCode.SUCCESS;
    }

    @GetMapping("/document-helper/bearer")
    public String doTestDocumentHelperForBearer(@Validated @NotBlank String url) {
        List<String> pathMatches = documentHelper.getBearerPathMatchs("test");
        List<String> excludePathMatches = documentHelper.getBearerExcludePathMatchs("test");
        long matchCount = pathMatches.stream().filter(o -> pathMatcher.match(o, url)).count();
        if (matchCount > 0) {
            if (excludePathMatches.stream().filter(o -> pathMatcher.match(o, url)).count() > 0) {
                matchCount = 0;
            }
        }
        return matchCount < 1 ? ReturnCode.FAILED : ReturnCode.SUCCESS;
    }

}

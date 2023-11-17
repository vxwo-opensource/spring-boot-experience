package org.vxwo.springboot.experience.web.testor;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@ComponentScan("org.vxwo.springboot.experience.web")
public class WebApplication {

    @GetMapping("/test-api-key")
    public String doTestApiKey() {
        return ReturnCode.SUCCESS;
    }

    @GetMapping("/test-bearer/include")
    public String doTestBearerInclude() {
        return ReturnCode.SUCCESS;
    }

    @GetMapping("/test-bearer/exclude")
    public String doTestBearerExclude() {
        return ReturnCode.SUCCESS;
    }

    @GetMapping("/test-bearer/optional")
    public String doTestBearerOptional(@RequestAttribute(ReturnCode.LOGINED) String logined) {
        return StringUtils.hasText(logined) ? ReturnCode.LOGINED : ReturnCode.SUCCESS;
    }

}

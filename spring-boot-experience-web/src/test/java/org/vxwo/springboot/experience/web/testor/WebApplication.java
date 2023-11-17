package org.vxwo.springboot.experience.web.testor;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@ComponentScan("org.vxwo.springboot.experience.web")
public class WebApplication {

    @GetMapping("/test-api-key")
    public String doTestApiKey() {
        return ReturnCode.SUCCESS;
    }
}

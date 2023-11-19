package org.vxwo.springboot.experience.web.testor;

import java.util.HashMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import static org.vxwo.springboot.experience.web.testor.CustomRequestBody.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {WebApplication.class})
public class ApplicationTest {
    @LocalServerPort
    private int localPort;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @Order(101)
    public void testApiKeyShouldReturnSuccess() {
        RequestEntity<?> request =
                RequestEntity.get(String.format("http://localhost:%s/test-api-key", localPort))
                        .header("api-key", "test-key").build();
        ResponseEntity<String> response = this.restTemplate.exchange(request, String.class);
        Assertions.assertEquals(ReturnCode.SUCCESS, response.getBody());
    }

    @Test
    @Order(102)
    public void testApiKeyShouldReturnFailed() {
        RequestEntity<?> request = RequestEntity
                .get(String.format("http://localhost:%s/test-api-key", localPort)).build();
        ResponseEntity<String> response = this.restTemplate.exchange(request, String.class);
        Assertions.assertEquals(ReturnCode.FAILED, response.getBody());
    }

    @Test
    @Order(103)
    public void testBearerIncludeShouldReturnFailed() {
        RequestEntity<?> request = RequestEntity
                .get(String.format("http://localhost:%s/test-bearer/include", localPort)).build();
        ResponseEntity<String> response = this.restTemplate.exchange(request, String.class);
        Assertions.assertEquals(ReturnCode.FAILED, response.getBody());
    }

    @Test
    @Order(104)
    public void testBearerExcludeShouldReturnSuccess() {
        RequestEntity<?> request = RequestEntity
                .get(String.format("http://localhost:%s/test-bearer/exclude", localPort)).build();
        ResponseEntity<String> response = this.restTemplate.exchange(request, String.class);
        Assertions.assertEquals(ReturnCode.SUCCESS, response.getBody());
    }

    @Test
    @Order(105)
    public void testBearerOptionalShouldReturnLogined() {
        RequestEntity<?> request = RequestEntity
                .get(String.format("http://localhost:%s/test-bearer/optional", localPort))
                .header(HttpHeaders.AUTHORIZATION, "Bearer testor").build();
        ResponseEntity<String> response = this.restTemplate.exchange(request, String.class);
        Assertions.assertEquals(ReturnCode.LOGINED, response.getBody());
    }

    @Test
    @Order(1010)
    public void testFrequencyConcurrencyShouldReturnSuccess() {
        RequestEntity<?> request = RequestEntity
                .get(String.format("http://localhost:%s/test-frequency/concurrency", localPort))
                .build();
        ResponseEntity<String> response = this.restTemplate.exchange(request, String.class);
        Assertions.assertEquals(ReturnCode.SUCCESS, response.getBody());
    }

    @Test
    @Order(1011)
    public void testFrequencyConcurrencyTiiceShouldReturnSuccess() {
        RequestEntity<?> request = RequestEntity
                .get(String.format("http://localhost:%s/test-frequency/concurrency", localPort))
                .build();
        ResponseEntity<String> response = this.restTemplate.exchange(request, String.class);
        Assertions.assertEquals(ReturnCode.SUCCESS, response.getBody());
    }

    @Test
    @Order(1020)
    public void testFrequencyFixedIntervalShouldReturnSuccess() {
        RequestEntity<?> request = RequestEntity
                .get(String.format("http://localhost:%s/test-frequency/fixed-interval", localPort))
                .build();
        ResponseEntity<String> response = this.restTemplate.exchange(request, String.class);
        Assertions.assertEquals(ReturnCode.SUCCESS, response.getBody());
    }

    @Test
    @Order(1021)
    public void testFrequencyFixedIntervalTwiceShouldReturnFailed() {
        RequestEntity<?> request = RequestEntity
                .get(String.format("http://localhost:%s/test-frequency/fixed-interval", localPort))
                .build();
        ResponseEntity<String> response = this.restTemplate.exchange(request, String.class);
        Assertions.assertEquals(ReturnCode.FAILED, response.getBody());
    }

    @Test
    @Order(1030)
    public void testValidationChoiceShouldReturnNoValid() {
        ChoicesBody body = new ChoicesBody();
        body.setV("c");
        String response = this.restTemplate.postForObject(
                String.format("http://localhost:%s/test-validation/choices", localPort), body,
                String.class);
        Assertions.assertEquals(ReturnCode.NO_VALID, response);
    }

    @Test
    @Order(1031)
    public void testValidationMultiChoiceShouldReturnSuccess() {
        MultiChoicesBody body = new MultiChoicesBody();
        body.setV("c,a");
        String response = this.restTemplate.postForObject(
                String.format("http://localhost:%s/test-validation/multi-choices", localPort), body,
                String.class);
        Assertions.assertEquals(ReturnCode.SUCCESS, response);
    }

    @Test
    @Order(1032)
    public void testValidationMultiPatternShouldReturnSuccess() {
        MultiPatternBody body = new MultiPatternBody();
        body.setV("9");
        String response = this.restTemplate.postForObject(
                String.format("http://localhost:%s/test-validation/multi-pattern", localPort), body,
                String.class);
        Assertions.assertEquals(ReturnCode.SUCCESS, response);
    }

    @Test
    @Order(1033)
    public void testValidationMultiPatternShouldReturnNovalid() {
        MultiPatternBody body = new MultiPatternBody();
        body.setV("b");
        String response = this.restTemplate.postForObject(
                String.format("http://localhost:%s/test-validation/multi-pattern", localPort), body,
                String.class);
        Assertions.assertEquals(ReturnCode.NO_VALID, response);
    }

    @Test
    @Order(1034)
    public void testValidationMultiPatternReserveShouldReturnSuccess() {
        MultiPatternBody body = new MultiPatternBody();
        body.setV("B");
        String response = this.restTemplate.postForObject(
                String.format("http://localhost:%s/test-validation/multi-pattern", localPort), body,
                String.class);
        Assertions.assertEquals(ReturnCode.SUCCESS, response);
    }
}

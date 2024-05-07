package tester;

import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.vxwo.springboot.experience.web.entity.RequestLoggingEntity;
import org.vxwo.springboot.experience.web.processor.RequestLoggingHelper;
import tester.CustomRequestBody.ChoicesBody;
import tester.CustomRequestBody.MultiChoicesBody;
import tester.CustomRequestBody.MultiPatternBody;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTest {
    @LocalServerPort
    private int localPort;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RequestLoggingHelper requestLoggingHelper;

    @Test
    @Order(101)
    public void testApiKeyShouldReturnSuccess() {
        RequestEntity<?> request =
                RequestEntity.get(String.format("http://localhost:%s/tester/api-key", localPort))
                        .header("api-key", "test-key").build();
        ResponseEntity<String> response = this.restTemplate.exchange(request, String.class);
        Assertions.assertEquals(ReturnCode.SUCCESS, response.getBody());
    }

    @Test
    @Order(102)
    public void testApiKeyShouldReturnFailed() {
        RequestEntity<?> request = RequestEntity
                .get(String.format("http://localhost:%s/tester/api-key", localPort)).build();
        ResponseEntity<String> response = this.restTemplate.exchange(request, String.class);
        Assertions.assertEquals(ReturnCode.FAILED, response.getBody());
    }

    @Test
    @Order(103)
    public void testBearerIncludeShouldReturnSuccess() {
        RequestEntity<?> request = RequestEntity
                .get(String.format("http://localhost:%s/tester/bearer/include-path", localPort))
                .header(HttpHeaders.AUTHORIZATION, "Bearer tester").build();
        ResponseEntity<String> response = this.restTemplate.exchange(request, String.class);
        Assertions.assertEquals(ReturnCode.SUCCESS, response.getBody());
    }

    @Test
    @Order(103)
    public void testBearerIncludeShouldReturnFailed() {
        RequestEntity<?> request = RequestEntity
                .get(String.format("http://localhost:%s/tester/bearer/include-path", localPort))
                .build();
        ResponseEntity<String> response = this.restTemplate.exchange(request, String.class);
        Assertions.assertEquals(ReturnCode.FAILED, response.getBody());
    }

    @Test
    @Order(104)
    public void testBearerExcludeShouldReturnSuccess() {
        RequestEntity<?> request = RequestEntity
                .get(String.format("http://localhost:%s/tester/bearer/exclude-path", localPort))
                .build();
        ResponseEntity<String> response = this.restTemplate.exchange(request, String.class);
        Assertions.assertEquals(ReturnCode.SUCCESS, response.getBody());
    }

    @Test
    @Order(105)
    public void testBearerLoginedOptionalShouldReturnLogined() {
        RequestEntity<?> request = RequestEntity
                .get(String.format("http://localhost:%s/tester/bearer/optional-path", localPort))
                .header(HttpHeaders.AUTHORIZATION, "Bearer tester").build();
        ResponseEntity<String> response = this.restTemplate.exchange(request, String.class);
        Assertions.assertEquals(ReturnCode.LOGINED, response.getBody());
    }

    @Test
    @Order(105)
    public void testBearerUnloginedOptionalShouldReturnSuccess() {
        RequestEntity<?> request = RequestEntity
                .get(String.format("http://localhost:%s/tester/bearer/optional-path", localPort))
                .build();
        ResponseEntity<String> response = this.restTemplate.exchange(request, String.class);
        Assertions.assertEquals(ReturnCode.SUCCESS, response.getBody());
    }

    @Test
    @Order(121)
    public void testManualIncludeShouldReturnFailed() {
        RequestEntity<?> request = RequestEntity
                .get(String.format("http://localhost:%s/tester/manual/include-path", localPort))
                .build();
        ResponseEntity<String> response = this.restTemplate.exchange(request, String.class);
        Assertions.assertEquals(ReturnCode.FAILED, response.getBody());
    }

    @Test
    @Order(122)
    public void testManualExcludeShouldReturnSuccess() {
        RequestEntity<?> request = RequestEntity
                .get(String.format("http://localhost:%s/tester/manual/exclude-path", localPort))
                .build();
        ResponseEntity<String> response = this.restTemplate.exchange(request, String.class);
        Assertions.assertEquals(ReturnCode.SUCCESS, response.getBody());
    }

    @Test
    @Order(123)
    public void testManualLoginedOptionalShouldReturnLogined() {
        RequestEntity<?> request = RequestEntity
                .get(String.format("http://localhost:%s/tester/manual/optional-path", localPort))
                .header(Constants.MANUAL_KEY, "tester").build();
        ResponseEntity<String> response = this.restTemplate.exchange(request, String.class);
        Assertions.assertEquals(ReturnCode.LOGINED, response.getBody());
    }

    @Test
    @Order(124)
    public void testManualUnloginedOptionalShouldReturnSuccess() {
        RequestEntity<?> request = RequestEntity
                .get(String.format("http://localhost:%s/tester/manual/optional-path", localPort))
                .build();
        ResponseEntity<String> response = this.restTemplate.exchange(request, String.class);
        Assertions.assertEquals(ReturnCode.SUCCESS, response.getBody());
    }

    @Test
    @Order(1010)
    public void testFrequencyConcurrencyShouldReturnSuccess() {
        RequestEntity<?> request = RequestEntity
                .get(String.format("http://localhost:%s/tester/frequency/concurrency", localPort))
                .build();
        ResponseEntity<String> response = this.restTemplate.exchange(request, String.class);
        Assertions.assertEquals(ReturnCode.SUCCESS, response.getBody());
    }

    @Test
    @Order(1011)
    public void testFrequencyConcurrencyTiiceShouldReturnSuccess() {
        RequestEntity<?> request = RequestEntity
                .get(String.format("http://localhost:%s/tester/frequency/concurrency", localPort))
                .build();
        ResponseEntity<String> response = this.restTemplate.exchange(request, String.class);
        Assertions.assertEquals(ReturnCode.SUCCESS, response.getBody());
    }

    @Test
    @Order(1020)
    public void testFrequencyFixedIntervalShouldReturnSuccess() {
        RequestEntity<?> request = RequestEntity.get(
                String.format("http://localhost:%s/tester/frequency/fixed-interval", localPort))
                .build();
        ResponseEntity<String> response = this.restTemplate.exchange(request, String.class);
        Assertions.assertEquals(ReturnCode.SUCCESS, response.getBody());
    }

    @Test
    @Order(1021)
    public void testFrequencyFixedIntervalTwiceShouldReturnFailed() {
        RequestEntity<?> request = RequestEntity.get(
                String.format("http://localhost:%s/tester/frequency/fixed-interval", localPort))
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
                String.format("http://localhost:%s/tester/validation/choices", localPort), body,
                String.class);
        Assertions.assertEquals(ReturnCode.NO_VALID, response);
    }

    @Test
    @Order(1031)
    public void testValidationMultiChoiceShouldReturnSuccess() {
        MultiChoicesBody body = new MultiChoicesBody();
        body.setV("c,a");
        String response = this.restTemplate.postForObject(
                String.format("http://localhost:%s/tester/validation/multi-choices", localPort),
                body, String.class);
        Assertions.assertEquals(ReturnCode.SUCCESS, response);
    }

    @Test
    @Order(1032)
    public void testValidationMultiPatternShouldReturnSuccess() {
        MultiPatternBody body = new MultiPatternBody();
        body.setV("9");
        String response = this.restTemplate.postForObject(
                String.format("http://localhost:%s/tester/validation/multi-pattern", localPort),
                body, String.class);
        Assertions.assertEquals(ReturnCode.SUCCESS, response);
    }

    @Test
    @Order(1033)
    public void testValidationMultiPatternShouldReturnNovalid() {
        MultiPatternBody body = new MultiPatternBody();
        body.setV("b");
        String response = this.restTemplate.postForObject(
                String.format("http://localhost:%s/tester/validation/multi-pattern", localPort),
                body, String.class);
        Assertions.assertEquals(ReturnCode.NO_VALID, response);
    }

    @Test
    @Order(1034)
    public void testValidationMultiPatternReserveShouldReturnSuccess() {
        MultiPatternBody body = new MultiPatternBody();
        body.setV("B");
        String response = this.restTemplate.postForObject(
                String.format("http://localhost:%s/tester/validation/multi-pattern", localPort),
                body, String.class);
        Assertions.assertEquals(ReturnCode.SUCCESS, response);
    }

    @Test
    @Order(1101)
    public void testDocumentHelperForApiKey1ReturnSuccess() {
        String response = this.restTemplate.getForObject(String.format(
                "http://localhost:%s/tester/document-helper/apikey?url=/tester/api-key", localPort),
                String.class);
        Assertions.assertEquals(ReturnCode.SUCCESS, response);
    }

    @Test
    @Order(1102)
    public void testDocumentHelperForApiKey2ReturnSuccess() {
        String response = this.restTemplate.getForObject(String.format(
                "http://localhost:%s/tester/document-helper/apikey?url=/tester/api-key/aaaaa",
                localPort), String.class);
        Assertions.assertEquals(ReturnCode.SUCCESS, response);
    }

    @Test
    @Order(1103)
    public void testDocumentHelperForApiKeyReturnFailed() {
        String response = this.restTemplate.getForObject(String.format(
                "http://localhost:%s/tester/document-helper/apikey?url=/tester/api-key-bb",
                localPort), String.class);
        Assertions.assertEquals(ReturnCode.FAILED, response);
    }

    @Test
    @Order(1111)
    public void testDocumentHelperForBearerReturnSuccess() {
        String response = this.restTemplate.getForObject(String.format(
                "http://localhost:%s/tester/document-helper/bearer?url=/tester/bearer/a",
                localPort), String.class);
        Assertions.assertEquals(ReturnCode.SUCCESS, response);
    }

    @Test
    @Order(1112)
    public void testDocumentHelperForBearerExcludeReturnFailed() {
        String response = this.restTemplate.getForObject(String.format(
                "http://localhost:%s/tester/document-helper/bearer?url=/tester/bearer/exclude-path",
                localPort), String.class);
        Assertions.assertEquals(ReturnCode.FAILED, response);
    }

    @Test
    @Order(1113)
    public void testDocumentHelperForBearerOptionalReturnFailed() {
        String response = this.restTemplate.getForObject(String.format(
                "http://localhost:%s/tester/document-helper/bearer?url=/tester/bearer/optional-path",
                localPort), String.class);
        Assertions.assertEquals(ReturnCode.FAILED, response);
    }

    @Test
    @Order(1201)
    public void testRequestLoggingHelperWebReturnSuccess() {
        String response = this.restTemplate.getForObject(
                String.format("http://localhost:%s/tester/chaos/RequestLoggingHelper", localPort),
                String.class);
        Assertions.assertEquals(ReturnCode.SUCCESS, response);
    }

    @Test
    @Order(2001)
    public void testRequestLoggingHelper() {
        String owner = UUID.randomUUID().toString();

        RequestLoggingEntity first = requestLoggingHelper.createLocalEntity();
        requestLoggingHelper.putOwner(owner);
        Assertions.assertEquals(true, owner.equals(first.getOwner()));

        requestLoggingHelper.removeLocalEntity();
        requestLoggingHelper.putOwner("");
        Assertions.assertEquals(true, owner.equals(first.getOwner()));

        RequestLoggingEntity second = requestLoggingHelper.createLocalEntity();
        Assertions.assertEquals(false, owner.equals(second.getOwner()));
    }
}

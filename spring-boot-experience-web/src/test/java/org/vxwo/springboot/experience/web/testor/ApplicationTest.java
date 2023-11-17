package org.vxwo.springboot.experience.web.testor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {WebApplication.class})
public class ApplicationTest {
    @LocalServerPort
    private int localPort;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testApiKeyShouldReturnSuccess() {
        RequestEntity<?> request =
                RequestEntity.get(String.format("http://localhost:%s/test-api-key", localPort))
                        .header("api-key", "test-key").build();
        ResponseEntity<String> response = this.restTemplate.exchange(request, String.class);
        Assertions.assertEquals(ReturnCode.SUCCESS, response.getBody());
    }

    @Test
    public void testApiKeyShouldReturnFailed() {
        RequestEntity<?> request = RequestEntity
                .get(String.format("http://localhost:%s/test-api-key", localPort)).build();
        ResponseEntity<String> response = this.restTemplate.exchange(request, String.class);
        Assertions.assertEquals(ReturnCode.FAILED, response.getBody());
    }
}

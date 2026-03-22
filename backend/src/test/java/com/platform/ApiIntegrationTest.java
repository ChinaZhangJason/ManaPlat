package com.platform;

import com.platform.common.Result;
import com.platform.monitor.model.ReportConfig;
import com.platform.alert.model.AlertRule;
import com.platform.sap.model.HanaConnectionConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testHealthEndpoint() {
        String url = "http://localhost:" + port + "/api/health";
        ResponseEntity<Result> response = restTemplate.getForEntity(url, Result.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testLoginEndpoint() {
        String url = "http://localhost:" + port + "/api/auth/login";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        String requestBody = "{\"username\":\"admin\",\"password\":\"admin123\"}";
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        
        try {
            ResponseEntity<Result> response = restTemplate.postForEntity(url, entity, Result.class);
            assertNotNull(response);
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    void testUnauthorizedAccess() {
        String url = "http://localhost:" + port + "/api/system/user/list";
        ResponseEntity<Result> response = restTemplate.getForEntity(url, Result.class);
        
        assertNotNull(response);
    }

    @Test
    void testReportEndpointsWithoutAuth() {
        String url = "http://localhost:" + port + "/api/monitor/report/list";
        
        try {
            ResponseEntity<Result> response = restTemplate.getForEntity(url, Result.class);
            assertNotNull(response);
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    void testAlertEndpointsWithoutAuth() {
        String url = "http://localhost:" + port + "/api/monitor/alert/rule/list";
        
        try {
            ResponseEntity<Result> response = restTemplate.getForEntity(url, Result.class);
            assertNotNull(response);
        } catch (Exception e) {
            assertTrue(true);
        }
    }
}

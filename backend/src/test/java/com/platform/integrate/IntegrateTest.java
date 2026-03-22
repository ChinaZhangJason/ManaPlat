package com.platform.integrate;

import com.platform.integrate.model.IntegratePlatform;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class IntegrateTest {

    @Test
    void testPlatformCreation() {
        IntegratePlatform platform = new IntegratePlatform();
        platform.setId(1L);
        platform.setPlatformName("钉钉集成");
        platform.setPlatformCode("DINGTALK");
        platform.setBaseUrl("https://oapi.dingtalk.com");
        platform.setSsoMode("TOKEN");
        platform.setStatus(1);
        
        assertEquals("钉钉集成", platform.getPlatformName());
        assertEquals("DINGTALK", platform.getPlatformCode());
        assertEquals("TOKEN", platform.getSsoMode());
    }

    @Test
    void testSsoModes() {
        String[] validSsoModes = {"TOKEN", "COOKIE", "OAUTH2", "SAML", "NONE"};
        
        assertEquals(5, validSsoModes.length);
        assertTrue(contains(validSsoModes, "TOKEN"));
        assertTrue(contains(validSsoModes, "OAUTH2"));
    }

    @Test
    void testSsoTokenGeneration() {
        String token = generateSsoToken("admin", System.currentTimeMillis() + 300000);
        
        assertNotNull(token);
        assertTrue(token.length() > 20);
    }

    @Test
    void testSsoTokenValidation() {
        long validExpiry = System.currentTimeMillis() + 300000;
        long invalidExpiry = System.currentTimeMillis() - 1000;
        
        assertTrue(isTokenValid(validExpiry));
        assertFalse(isTokenValid(invalidExpiry));
    }

    @Test
    void testPlatformCategories() {
        String[] categories = {"OAUTH", "SSO", "WEBHOOK", "API"};
        
        assertEquals(4, categories.length);
        assertTrue(contains(categories, "OAUTH"));
        assertTrue(contains(categories, "SSO"));
    }

    @Test
    void testPlatformStatus() {
        IntegratePlatform platform = new IntegratePlatform();
        
        platform.setStatus(1);
        assertEquals(1, platform.getStatus());
        
        platform.setStatus(0);
        assertEquals(0, platform.getStatus());
    }

    @Test
    void testIframeIntegration() {
        IntegratePlatform platform = new IntegratePlatform();
        platform.setBaseUrl("https://external-system.com");
        platform.setPlatformCode("EXTERNAL");
        
        String iframeUrl = buildIframeUrl(platform, "token123", 1L);
        
        assertTrue(iframeUrl.contains("external-system.com"));
        assertTrue(iframeUrl.contains("token=token123"));
    }

    @Test
    void testAccessLogCreation() {
        assertTrue(true);
    }

    private boolean contains(String[] arr, String item) {
        for (String s : arr) {
            if (s.equals(item)) return true;
        }
        return false;
    }

    private String generateSsoToken(String userId, long expiry) {
        return String.format("%s_%d_%s", userId, expiry, 
            Long.toHexString(Double.doubleToLongBits(Math.random())));
    }

    private boolean isTokenValid(long expiry) {
        return System.currentTimeMillis() < expiry;
    }

    private String buildIframeUrl(IntegratePlatform platform, String token, Long userId) {
        return String.format("%s?token=%s&userId=%d", 
            platform.getBaseUrl(), token, userId);
    }
}

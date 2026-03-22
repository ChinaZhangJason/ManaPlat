package com.platform.auth;

import com.platform.auth.dto.LoginRequest;
import com.platform.auth.dto.RegisterRequest;
import com.platform.auth.dto.SendCodeRequest;
import com.platform.auth.dto.ResetPasswordRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthDtoTest {

    @Test
    void testLoginRequest() {
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("admin123");
        
        assertEquals("admin", request.getUsername());
        assertEquals("admin123", request.getPassword());
    }

    @Test
    void testRegisterRequest() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("newuser");
        request.setPassword("password123");
        request.setEmail("newuser@example.com");
        request.setPhone("13800138000");
        
        assertEquals("newuser", request.getUsername());
        assertEquals("password123", request.getPassword());
        assertEquals("newuser@example.com", request.getEmail());
        assertEquals("13800138000", request.getPhone());
    }

    @Test
    void testSendCodeRequest() {
        SendCodeRequest request = new SendCodeRequest();
        request.setType("EMAIL");
        request.setTarget("test@example.com");
        request.setScene("RESET_PASSWORD");
        
        assertEquals("EMAIL", request.getType());
        assertEquals("test@example.com", request.getTarget());
        assertEquals("RESET_PASSWORD", request.getScene());
    }

    @Test
    void testSendCodeRequestWithPhone() {
        SendCodeRequest request = new SendCodeRequest();
        request.setType("SMS");
        request.setTarget("13800138000");
        request.setScene("LOGIN");
        
        assertEquals("SMS", request.getType());
        assertEquals("13800138000", request.getTarget());
    }

    @Test
    void testResetPasswordRequest() {
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setType("email");
        request.setTarget("test@example.com");
        request.setCode("123456");
        request.setNewPassword("newPassword123");
        
        assertEquals("email", request.getType());
        assertEquals("test@example.com", request.getTarget());
        assertEquals("123456", request.getCode());
        assertEquals("newPassword123", request.getNewPassword());
    }

    @Test
    void testCodeGeneration() {
        int code = (int) ((Math.random() * 900000) + 100000);
        
        assertTrue(code >= 100000);
        assertTrue(code <= 999999);
        assertEquals(6, String.valueOf(code).length());
    }

    @Test
    void testCodeExpiration() {
        long currentTime = System.currentTimeMillis();
        long expireTime = currentTime + 10 * 60 * 1000;
        long fiveMinutesLater = currentTime + 5 * 60 * 1000;
        long fifteenMinutesLater = currentTime + 15 * 60 * 1000;
        
        assertTrue(currentTime < expireTime);
        assertTrue(fiveMinutesLater < expireTime);
        assertTrue(fifteenMinutesLater > expireTime);
    }

    @Test
    void testPasswordValidation() {
        String password = "password123";
        
        assertTrue(password.length() >= 6);
        assertTrue(password.length() <= 20);
        assertFalse(password.contains(" "));
    }

    @Test
    void testEmailValidation() {
        String email = "test@example.com";
        boolean isValid = email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
        
        assertTrue(isValid);
    }

    @Test
    void testPhoneValidation() {
        String phone = "13800138000";
        boolean isValid = phone.matches("^1[3-9]\\d{9}$");
        
        assertTrue(isValid);
    }
}

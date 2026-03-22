package com.platform.auth;

import com.platform.auth.dto.LoginRequest;
import com.platform.auth.dto.AuthResponse;
import com.platform.auth.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class AuthServiceTest {

    private AuthService authService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testLogin_Success() {
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("admin123");
        
        AuthResponse result = authService.login(request);
        assertNotNull(result);
    }

    @Test
    void testLogin_Failed() {
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("wrong");
        
        assertThrows(Exception.class, () -> authService.login(request));
    }

    @Test
    void testGetUserInfo() {
        assertNotNull(authService.getUserInfo("admin"));
    }
}

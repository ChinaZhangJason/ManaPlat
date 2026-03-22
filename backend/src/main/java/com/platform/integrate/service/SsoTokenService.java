package com.platform.integrate.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Service
public class SsoTokenService {

    @Value("${sso.token-secret:defaultSecretKeyForSsoTokenMustBeAtLeast256Bits}")
    private String tokenSecret;

    @Value("${sso.token-expiration:300000}")
    private long tokenExpiration;

    public String generateToken(Long userId, String username, String platformCode, String platformSecret) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + tokenExpiration);
        
        String tokenId = UUID.randomUUID().toString();
        
        SecretKey key = Keys.hmacShaKeyFor(platformSecret.getBytes(StandardCharsets.UTF_8));
        
        return Jwts.builder()
                .id(tokenId)
                .subject(userId.toString())
                .claim("username", username)
                .claim("platformCode", platformCode)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token, String platformSecret) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(platformSecret.getBytes(StandardCharsets.UTF_8));
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Claims parseToken(String token, String platformSecret) {
        SecretKey key = Keys.hmacShaKeyFor(platformSecret.getBytes(StandardCharsets.UTF_8));
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public SsoTokenInfo getTokenInfo(String token, String platformSecret) {
        Claims claims = parseToken(token, platformSecret);
        SsoTokenInfo info = new SsoTokenInfo();
        info.setUserId(Long.parseLong(claims.getSubject()));
        info.setUsername(claims.get("username", String.class));
        info.setPlatformCode(claims.get("platformCode", String.class));
        info.setTokenId(claims.getId());
        info.setIssuedAt(claims.getIssuedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        info.setExpiresAt(claims.getExpiration().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        return info;
    }

    public String generateSsoUrl(String platformCode, String baseUrl, String ssoEndpoint, Long userId, String username, String platformSecret) {
        String token = generateToken(userId, username, platformCode, platformSecret);
        String separator = ssoEndpoint.contains("?") ? "&" : "?";
        return baseUrl + ssoEndpoint + separator + "token=" + token;
    }

    public static class SsoTokenInfo {
        private Long userId;
        private String username;
        private String platformCode;
        private String tokenId;
        private LocalDateTime issuedAt;
        private LocalDateTime expiresAt;
        
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPlatformCode() { return platformCode; }
        public void setPlatformCode(String platformCode) { this.platformCode = platformCode; }
        public String getTokenId() { return tokenId; }
        public void setTokenId(String tokenId) { this.tokenId = tokenId; }
        public LocalDateTime getIssuedAt() { return issuedAt; }
        public void setIssuedAt(LocalDateTime issuedAt) { this.issuedAt = issuedAt; }
        public LocalDateTime getExpiresAt() { return expiresAt; }
        public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
    }
}

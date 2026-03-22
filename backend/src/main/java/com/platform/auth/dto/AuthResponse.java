package com.platform.auth.dto;

public class AuthResponse {
    private String token;
    private String refreshToken;
    private UserInfo userInfo;

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
    public UserInfo getUserInfo() { return userInfo; }
    public void setUserInfo(UserInfo userInfo) { this.userInfo = userInfo; }

    public static class UserInfo {
        private Long id;
        private String username;
        private String email;
        private String phone;
        
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        
        public static UserInfoBuilder builder() { return new UserInfoBuilder(); }
        
        public static class UserInfoBuilder {
            private Long id;
            private String username;
            private String email;
            private String phone;
            
            public UserInfoBuilder id(Long id) { this.id = id; return this; }
            public UserInfoBuilder username(String username) { this.username = username; return this; }
            public UserInfoBuilder email(String email) { this.email = email; return this; }
            public UserInfoBuilder phone(String phone) { this.phone = phone; return this; }
            public UserInfo build() {
                UserInfo info = new UserInfo();
                info.id = this.id;
                info.username = this.username;
                info.email = this.email;
                info.phone = this.phone;
                return info;
            }
        }
    }
    
    public static Builder builder() { return new Builder(); }
    
    public static class Builder {
        private String token;
        private String refreshToken;
        private UserInfo userInfo;
        
        public Builder token(String token) { this.token = token; return this; }
        public Builder refreshToken(String refreshToken) { this.refreshToken = refreshToken; return this; }
        public Builder userInfo(UserInfo userInfo) { this.userInfo = userInfo; return this; }
        public AuthResponse build() {
            AuthResponse response = new AuthResponse();
            response.token = this.token;
            response.refreshToken = this.refreshToken;
            response.userInfo = this.userInfo;
            return response;
        }
    }
}

package com.platform.common;

public class Constants {
    public static final String JWT_HEADER = "Authorization";
    public static final String JWT_PREFIX = "Bearer ";
    
    public static final String ROLE_SUPER_ADMIN = "SUPER_ADMIN";
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_USER = "USER";
    
    public static final Integer STATUS_ACTIVE = 1;
    public static final Integer STATUS_INACTIVE = 0;
    
    public static final String OAUTH2_DINGTALK = "dingtalk";
    public static final String OAUTH2_GENERIC = "oauth2";
    
    public static final int CACHE_SHORT_TTL = 60;
    public static final int CACHE_MEDIUM_TTL = 300;
    public static final int CACHE_LONG_TTL = 3600;
}

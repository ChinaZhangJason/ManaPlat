package com.platform.common;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final int code = 400;
    
    public BusinessException(String message) {
        super(message);
    }
    
    public BusinessException(int code, String message) {
        super(message);
    }
}

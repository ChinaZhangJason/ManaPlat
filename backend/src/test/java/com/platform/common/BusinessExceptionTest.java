package com.platform.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BusinessExceptionTest {

    @Test
    void testBusinessExceptionCreation() {
        BusinessException exception = new BusinessException("业务错误");
        
        assertEquals("业务错误", exception.getMessage());
        assertEquals(500, BusinessException.DEFAULT_CODE);
    }

    @Test
    void testBusinessExceptionWithCode() {
        BusinessException exception = new BusinessException(400, "参数错误");
        
        assertEquals("参数错误", exception.getMessage());
    }

    @Test
    void testBusinessExceptionWithCause() {
        RuntimeException cause = new RuntimeException("原始错误");
        BusinessException exception = new BusinessException(500, "业务错误", cause);
        
        assertEquals("业务错误", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testBusinessExceptionIsRuntimeException() {
        BusinessException exception = new BusinessException("test");
        
        assertTrue(exception instanceof RuntimeException);
    }
}

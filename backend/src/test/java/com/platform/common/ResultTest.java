package com.platform.common;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ResultTest {

    @Test
    void testSuccessResult() {
        Result<String> result = Result.success("test data");
        
        assertEquals(200, result.getCode());
        assertEquals("success", result.getMessage());
        assertEquals("test data", result.getData());
        assertNotNull(result.getTimestamp());
    }

    @Test
    void testSuccessResultWithMessage() {
        Result<String> result = Result.success("操作成功", "data");
        
        assertEquals(200, result.getCode());
        assertEquals("操作成功", result.getMessage());
    }

    @Test
    void testErrorResult() {
        Result<Void> result = Result.error("发生错误");
        
        assertEquals(500, result.getCode());
        assertEquals("发生错误", result.getMessage());
        assertNull(result.getData());
    }

    @Test
    void testErrorResultWithCode() {
        Result<Void> result = Result.error(400, "参数错误");
        
        assertEquals(400, result.getCode());
        assertEquals("参数错误", result.getMessage());
    }

    @Test
    void testResultWithComplexData() {
        Map<String, Object> data = new java.util.HashMap<>();
        data.put("id", 1);
        data.put("name", "test");
        data.put("timestamp", LocalDateTime.now());
        Result<Object> result = Result.success(data);
        
        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
    }

    @Test
    void testResultTimestampIsSet() {
        Result<Void> result = Result.error("error");
        
        assertTrue(result.getTimestamp() > 0);
    }

    @Test
    void testResultGenerics() {
        Result<Integer> intResult = Result.success(123);
        Result<String> strResult = Result.success("test");
        Result<Object> objResult = Result.success(new Object());
        
        assertEquals(Integer.valueOf(123), intResult.getData());
        assertEquals("test", strResult.getData());
        assertNotNull(objResult.getData());
    }
}

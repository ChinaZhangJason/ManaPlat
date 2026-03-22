package com.platform.alert;

import com.platform.alert.model.AlertRule;
import com.platform.alert.model.AlertHistory;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AlertRuleTest {

    @Test
    void testAlertRuleCreation() {
        AlertRule rule = new AlertRule();
        rule.setId(1L);
        rule.setRuleName("CPU告警");
        rule.setRuleCode("CPU_ALERT");
        rule.setMetricType("SYSTEM_CPU");
        rule.setConditionType("GT");
        rule.setThresholdValue("80");
        rule.setTimeWindow(300);
        rule.setStatus(1);
        
        assertEquals("CPU告警", rule.getRuleName());
        assertEquals("CPU_ALERT", rule.getRuleCode());
        assertEquals("GT", rule.getConditionType());
        assertEquals(80, Integer.parseInt(rule.getThresholdValue()));
    }

    @Test
    void testConditionTypes() {
        Map<String, String> conditions = new java.util.HashMap<>();
        conditions.put("GT", "大于");
        conditions.put("LT", "小于");
        conditions.put("EQ", "等于");
        conditions.put("GE", "大于等于");
        conditions.put("LE", "小于等于");
        conditions.put("NE", "不等于");
        
        assertEquals("大于", conditions.get("GT"));
        assertEquals("小于", conditions.get("LT"));
        assertEquals("等于", conditions.get("EQ"));
        assertEquals(6, conditions.size());
    }

    @Test
    void testAlertHistoryStatus() {
        AlertHistory history = new AlertHistory();
        history.setId(1L);
        history.setRuleId(1L);
        history.setAlertLevel("WARNING");
        history.setStatus("PENDING");
        
        assertEquals("PENDING", history.getStatus());
        assertEquals("WARNING", history.getAlertLevel());
    }

    @Test
    void testAlertHistoryStatusTransitions() {
        AlertHistory history = new AlertHistory();
        
        history.setStatus("PENDING");
        assertEquals("PENDING", history.getStatus());
        
        history.setStatus("SENT");
        assertEquals("SENT", history.getStatus());
        
        history.setStatus("ACKED");
        assertEquals("ACKED", history.getStatus());
        
        history.setStatus("CLOSED");
        assertEquals("CLOSED", history.getStatus());
    }

    @Test
    void testAlertLevelTypes() {
        Map<String, String> levels = new java.util.HashMap<>();
        levels.put("INFO", "通知");
        levels.put("WARNING", "警告");
        levels.put("ERROR", "错误");
        levels.put("CRITICAL", "严重");
        
        assertEquals("警告", levels.get("WARNING"));
        assertEquals("错误", levels.get("ERROR"));
        assertEquals(4, levels.size());
    }

    @Test
    void testThresholdEvaluation() {
        double threshold = 80.0;
        
        assertTrue(evaluateCondition("GT", 90.0, threshold));
        assertFalse(evaluateCondition("GT", 80.0, threshold));
        assertFalse(evaluateCondition("GT", 70.0, threshold));
        
        assertTrue(evaluateCondition("LT", 70.0, threshold));
        assertFalse(evaluateCondition("LT", 80.0, threshold));
        
        assertTrue(evaluateCondition("GE", 80.0, threshold));
        assertTrue(evaluateCondition("GE", 90.0, threshold));
        assertFalse(evaluateCondition("GE", 70.0, threshold));
        
        assertTrue(evaluateCondition("LE", 80.0, threshold));
        assertTrue(evaluateCondition("LE", 70.0, threshold));
        assertFalse(evaluateCondition("LE", 90.0, threshold));
        
        assertTrue(evaluateCondition("EQ", 80.0, threshold));
        assertFalse(evaluateCondition("EQ", 90.0, threshold));
    }

    private boolean evaluateCondition(String condition, double value, double threshold) {
        return switch (condition) {
            case "GT" -> value > threshold;
            case "LT" -> value < threshold;
            case "GE" -> value >= threshold;
            case "LE" -> value <= threshold;
            case "EQ" -> value == threshold;
            default -> false;
        };
    }
}

package com.platform.alert;

import com.platform.alert.service.AlertRuleService;
import com.platform.alert.service.AlertReceiverService;
import com.platform.alert.model.AlertRule;
import com.platform.alert.model.AlertReceiver;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AlertServiceTest {

    @Autowired
    private AlertRuleService alertRuleService;

    @Autowired
    private AlertReceiverService alertReceiverService;

    @Test
    void testCreateAlertRule() {
        AlertRule rule = new AlertRule();
        rule.setRuleName("CPU告警");
        rule.setRuleCode("CPU_ALERT");
        rule.setMetricType("SYSTEM_CPU");
        rule.setConditionType("GT");
        rule.setThresholdValue("80");
        rule.setTimeWindow(300);

        AlertRule created = alertRuleService.create(rule);
        
        assertNotNull(created);
        assertNotNull(created.getId());
        assertEquals("CPU告警", created.getRuleName());
    }

    @Test
    void testListEnabledRules() {
        List<AlertRule> rules = alertRuleService.listEnabled();
        assertNotNull(rules);
    }

    @Test
    void testCreateReceiver() {
        AlertReceiver receiver = new AlertReceiver();
        receiver.setReceiverName("管理员");
        receiver.setReceiverType("USER");
        receiver.setUserId(1L);
        receiver.setNotifyChannels("[\"EMAIL\"]");

        AlertReceiver created = alertReceiverService.create(receiver);
        
        assertNotNull(created);
        assertEquals("管理员", created.getReceiverName());
    }

    @Test
    void testListReceivers() {
        List<AlertReceiver> receivers = alertReceiverService.listEnabled();
        assertNotNull(receivers);
    }
}

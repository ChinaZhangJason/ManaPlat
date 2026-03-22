package com.platform.alert.controller;

import com.platform.alert.model.AlertHistory;
import com.platform.alert.model.AlertReceiver;
import com.platform.alert.model.AlertRule;
import com.platform.alert.service.AlertHistoryService;
import com.platform.alert.service.AlertReceiverService;
import com.platform.alert.service.AlertRuleService;
import com.platform.common.Result;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/monitor/alert")
public class AlertController {

    private final AlertRuleService alertRuleService;
    private final AlertReceiverService alertReceiverService;
    private final AlertHistoryService alertHistoryService;

    public AlertController(
            AlertRuleService alertRuleService,
            AlertReceiverService alertReceiverService,
            AlertHistoryService alertHistoryService) {
        this.alertRuleService = alertRuleService;
        this.alertReceiverService = alertReceiverService;
        this.alertHistoryService = alertHistoryService;
    }

    @GetMapping("/rule/list")
    public Result<List<AlertRule>> listRules() {
        return Result.success(alertRuleService.list());
    }

    @GetMapping("/rule/{id}")
    public Result<AlertRule> getRule(@PathVariable Long id) {
        return Result.success(alertRuleService.getById(id));
    }

    @PostMapping("/rule")
    public Result<AlertRule> createRule(@RequestBody AlertRule rule) {
        return Result.success(alertRuleService.create(rule));
    }

    @PutMapping("/rule/{id}")
    public Result<AlertRule> updateRule(@PathVariable Long id, @RequestBody AlertRule rule) {
        return Result.success(alertRuleService.update(id, rule));
    }

    @DeleteMapping("/rule/{id}")
    public Result<Void> deleteRule(@PathVariable Long id) {
        alertRuleService.delete(id);
        return Result.success();
    }

    @GetMapping("/receiver/list")
    public Result<List<AlertReceiver>> listReceivers() {
        return Result.success(alertReceiverService.list());
    }

    @GetMapping("/receiver/{id}")
    public Result<AlertReceiver> getReceiver(@PathVariable Long id) {
        return Result.success(alertReceiverService.getById(id));
    }

    @PostMapping("/receiver")
    public Result<AlertReceiver> createReceiver(@RequestBody AlertReceiver receiver) {
        return Result.success(alertReceiverService.create(receiver));
    }

    @PutMapping("/receiver/{id}")
    public Result<AlertReceiver> updateReceiver(@PathVariable Long id, @RequestBody AlertReceiver receiver) {
        return Result.success(alertReceiverService.update(id, receiver));
    }

    @DeleteMapping("/receiver/{id}")
    public Result<Void> deleteReceiver(@PathVariable Long id) {
        alertReceiverService.delete(id);
        return Result.success();
    }

    @GetMapping("/history")
    public Result<List<AlertHistory>> getHistory(
            @RequestParam(required = false) Long ruleId,
            @RequestParam(defaultValue = "100") int limit) {
        List<AlertHistory> list;
        if (ruleId != null) {
            list = alertHistoryService.listByRule(ruleId, limit);
        } else {
            list = alertHistoryService.list();
        }
        return Result.success(list);
    }

    @PostMapping("/history/{id}/ack")
    public Result<Void> acknowledge(@PathVariable Long id, @RequestParam Long userId) {
        alertHistoryService.acknowledge(id, userId);
        return Result.success();
    }

    @PostMapping("/history/{id}/close")
    public Result<Void> close(@PathVariable Long id) {
        alertHistoryService.close(id);
        return Result.success();
    }
}

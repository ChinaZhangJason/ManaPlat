package com.platform.alert.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.alert.model.AlertRule;
import java.util.List;

public interface AlertRuleService extends IService<AlertRule> {
    List<AlertRule> listEnabled();
    AlertRule create(AlertRule rule);
    AlertRule update(Long id, AlertRule rule);
    void delete(Long id);
}

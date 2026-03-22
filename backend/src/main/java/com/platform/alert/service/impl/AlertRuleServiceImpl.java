package com.platform.alert.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.alert.mapper.AlertRuleMapper;
import com.platform.alert.model.AlertRule;
import com.platform.alert.service.AlertRuleService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlertRuleServiceImpl extends ServiceImpl<AlertRuleMapper, AlertRule> implements AlertRuleService {

    @Override
    public List<AlertRule> listEnabled() {
        return lambdaQuery().eq(AlertRule::getStatus, 1).list();
    }

    @Override
    public AlertRule create(AlertRule rule) {
        rule.setCreatedAt(LocalDateTime.now());
        rule.setUpdatedAt(LocalDateTime.now());
        rule.setStatus(1);
        this.save(rule);
        return rule;
    }

    @Override
    public AlertRule update(Long id, AlertRule rule) {
        rule.setId(id);
        rule.setUpdatedAt(LocalDateTime.now());
        this.updateById(rule);
        return rule;
    }

    @Override
    public void delete(Long id) {
        this.removeById(id);
    }
}

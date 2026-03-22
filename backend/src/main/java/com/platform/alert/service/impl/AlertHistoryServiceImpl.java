package com.platform.alert.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.alert.mapper.AlertHistoryMapper;
import com.platform.alert.model.AlertHistory;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlertHistoryServiceImpl extends ServiceImpl<AlertHistoryMapper, AlertHistory> implements AlertHistoryService {

    @Override
    public List<AlertHistory> listByRule(Long ruleId, int limit) {
        return lambdaQuery()
            .eq(AlertHistory::getRuleId, ruleId)
            .orderByDesc(AlertHistory::getCreatedAt)
            .last("LIMIT " + limit)
            .list();
    }

    @Override
    public void acknowledge(Long id, Long userId) {
        AlertHistory history = new AlertHistory();
        history.setId(id);
        history.setStatus("ACKED");
        history.setAckedBy(userId);
        history.setAckedAt(LocalDateTime.now());
        this.updateById(history);
    }

    @Override
    public void close(Long id) {
        AlertHistory history = new AlertHistory();
        history.setId(id);
        history.setStatus("CLOSED");
        this.updateById(history);
    }
}

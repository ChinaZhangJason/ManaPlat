package com.platform.alert.service;

import com.platform.alert.model.AlertHistory;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

public interface AlertHistoryService extends IService<AlertHistory> {
    List<AlertHistory> listByRule(Long ruleId, int limit);
    void acknowledge(Long id, Long userId);
    void close(Long id);
}

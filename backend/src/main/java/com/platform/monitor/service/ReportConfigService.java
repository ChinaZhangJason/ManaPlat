package com.platform.monitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.monitor.dto.ReportConfigRequest;
import com.platform.monitor.model.ReportConfig;

import java.util.List;

public interface ReportConfigService extends IService<ReportConfig> {
    List<ReportConfig> listByUser(Long userId);
    ReportConfig create(ReportConfigRequest request, Long userId);
    ReportConfig update(Long id, ReportConfigRequest request, Long userId);
    void delete(Long id);
    ReportConfig getWithWidgets(Long id);
}

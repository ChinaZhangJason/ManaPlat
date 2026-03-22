package com.platform.monitor.service;

import com.platform.monitor.model.MonitorSqlConfig;

import java.util.List;

public interface SqlConfigService {
    List<MonitorSqlConfig> listAll();
    
    MonitorSqlConfig getById(Long id);
    
    List<MonitorSqlConfig> getBySystemId(Long systemId);
    
    boolean save(MonitorSqlConfig config);
    
    boolean update(MonitorSqlConfig config);
    
    boolean deleteById(Long id);
}

package com.platform.monitor.service;

import java.util.List;
import java.util.Map;

public interface HanaQueryService {
    List<Map<String, Object>> executeQuery(Long systemId, String sql, Map<String, Object> params);
    
    Map<String, Object> executeQueryForSingle(Long systemId, String sql, Map<String, Object> params);
    
    boolean testConnection(Long systemId);
}

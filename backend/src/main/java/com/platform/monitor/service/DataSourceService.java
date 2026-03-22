package com.platform.monitor.service;

import com.platform.monitor.dto.WidgetDataRequest;

import java.util.Map;

public interface DataSourceService {
    String getType();
    Map<String, Object> query(WidgetDataRequest request) throws Exception;
    boolean testConnection(Map<String, Object> config) throws Exception;
}

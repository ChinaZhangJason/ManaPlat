package com.platform.monitor.dto;

import lombok.Data;

import java.util.Map;

@Data
public class WidgetDataRequest {
    private Long widgetId;
    private String dataSourceType;
    private Map<String, Object> dataSourceConfig;
    private Map<String, Object> filters;
    private String dateRange;
    private Long systemId;
}

package com.platform.monitor.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ReportConfigRequest {
    private Long id;
    private String reportName;
    private String reportCode;
    private String description;
    private Map<String, Object> layoutConfig;
    private Map<String, Object> filterConfig;
    private Integer refreshInterval;
    private Integer status;
    private List<WidgetConfig> widgets;
    
    @Data
    public static class WidgetConfig {
        private Long id;
        private String widgetName;
        private String widgetType;
        private String dataSourceType;
        private Map<String, Object> dataSourceConfig;
        private Map<String, Object> chartConfig;
        private Map<String, Object> positionConfig;
        private Integer sortOrder;
    }
}

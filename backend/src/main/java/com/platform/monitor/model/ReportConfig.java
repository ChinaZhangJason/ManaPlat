package com.platform.monitor.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.util.List;

@TableName("monitor_report_config")
public class ReportConfig {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String reportName;
    private String reportCode;
    private String description;
    @TableField("layout_config")
    private String layoutConfig;
    @TableField("filter_config")
    private String filterConfig;
    @TableField("refresh_interval")
    private Integer refreshInterval;
    private Integer status;
    @TableField("created_by")
    private Long createdBy;
    @TableField("created_at")
    private LocalDateTime createdAt;
    @TableField("updated_at")
    private LocalDateTime updatedAt;
    
    @TableField(exist = false)
    private List<ReportWidget> widgets;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getReportName() { return reportName; }
    public void setReportName(String reportName) { this.reportName = reportName; }
    public String getReportCode() { return reportCode; }
    public void setReportCode(String reportCode) { this.reportCode = reportCode; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getLayoutConfig() { return layoutConfig; }
    public void setLayoutConfig(String layoutConfig) { this.layoutConfig = layoutConfig; }
    public String getFilterConfig() { return filterConfig; }
    public void setFilterConfig(String filterConfig) { this.filterConfig = filterConfig; }
    public Integer getRefreshInterval() { return refreshInterval; }
    public void setRefreshInterval(Integer refreshInterval) { this.refreshInterval = refreshInterval; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public List<ReportWidget> getWidgets() { return widgets; }
    public void setWidgets(List<ReportWidget> widgets) { this.widgets = widgets; }
}

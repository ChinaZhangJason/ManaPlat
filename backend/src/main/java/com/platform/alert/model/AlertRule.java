package com.platform.alert.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

@TableName("monitor_alert_rule")
public class AlertRule {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String ruleName;
    private String ruleCode;
    private String metricType;
    private String conditionType;
    private String thresholdValue;
    @TableField("threshold_value_max")
    private String thresholdValueMax;
    @TableField("time_window")
    private Integer timeWindow;
    @TableField("system_id")
    private Long systemId;
    private Integer status;
    @TableField("created_by")
    private Long createdBy;
    @TableField("created_at")
    private LocalDateTime createdAt;
    @TableField("updated_at")
    private LocalDateTime updatedAt;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getRuleName() { return ruleName; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }
    public String getRuleCode() { return ruleCode; }
    public void setRuleCode(String ruleCode) { this.ruleCode = ruleCode; }
    public String getMetricType() { return metricType; }
    public void setMetricType(String metricType) { this.metricType = metricType; }
    public String getConditionType() { return conditionType; }
    public void setConditionType(String conditionType) { this.conditionType = conditionType; }
    public String getThresholdValue() { return thresholdValue; }
    public void setThresholdValue(String thresholdValue) { this.thresholdValue = thresholdValue; }
    public String getThresholdValueMax() { return thresholdValueMax; }
    public void setThresholdValueMax(String thresholdValueMax) { this.thresholdValueMax = thresholdValueMax; }
    public Integer getTimeWindow() { return timeWindow; }
    public void setTimeWindow(Integer timeWindow) { this.timeWindow = timeWindow; }
    public Long getSystemId() { return systemId; }
    public void setSystemId(Long systemId) { this.systemId = systemId; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}

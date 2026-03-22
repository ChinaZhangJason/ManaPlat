package com.platform.alert.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

@TableName("monitor_alert_history")
public class AlertHistory {
    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField("rule_id")
    private Long ruleId;
    private String alertLevel;
    private String metricValue;
    private String alertMessage;
    private String status;
    private LocalDateTime sentAt;
    private Long ackedBy;
    private LocalDateTime ackedAt;
    @TableField("created_at")
    private LocalDateTime createdAt;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getRuleId() { return ruleId; }
    public void setRuleId(Long ruleId) { this.ruleId = ruleId; }
    public String getAlertLevel() { return alertLevel; }
    public void setAlertLevel(String alertLevel) { this.alertLevel = alertLevel; }
    public String getMetricValue() { return metricValue; }
    public void setMetricValue(String metricValue) { this.metricValue = metricValue; }
    public String getAlertMessage() { return alertMessage; }
    public void setAlertMessage(String alertMessage) { this.alertMessage = alertMessage; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getSentAt() { return sentAt; }
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }
    public Long getAckedBy() { return ackedBy; }
    public void setAckedBy(Long ackedBy) { this.ackedBy = ackedBy; }
    public LocalDateTime getAckedAt() { return ackedAt; }
    public void setAckedAt(LocalDateTime ackedAt) { this.ackedAt = ackedAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

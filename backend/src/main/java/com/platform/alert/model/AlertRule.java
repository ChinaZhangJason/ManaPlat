package com.platform.alert.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
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
}

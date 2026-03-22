package com.platform.alert.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
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
}

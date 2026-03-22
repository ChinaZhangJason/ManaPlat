package com.platform.monitor.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
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
}

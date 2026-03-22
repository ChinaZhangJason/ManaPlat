package com.platform.monitor.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("monitor_report_widget")
public class ReportWidget {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("report_id")
    private Long reportId;
    
    @TableField("widget_name")
    private String widgetName;
    
    @TableField("widget_type")
    private String widgetType;
    
    @TableField("data_source_type")
    private String dataSourceType;
    
    @TableField("data_source_config")
    private String dataSourceConfig;
    
    @TableField("chart_config")
    private String chartConfig;
    
    @TableField("position_config")
    private String positionConfig;
    
    @TableField("sort_order")
    private Integer sortOrder;
    
    @TableField("created_at")
    private LocalDateTime createdAt;
    
    @TableField("updated_at")
    private LocalDateTime updatedAt;
}

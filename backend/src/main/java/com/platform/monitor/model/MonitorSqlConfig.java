package com.platform.monitor.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("monitor_sql_config")
public class MonitorSqlConfig {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String configName;
    
    private Long systemId;
    
    private String sqlTemplate;
    
    private String paramDefs;
    
    private String chartType;
    
    private String description;
    
    private Integer status;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
}

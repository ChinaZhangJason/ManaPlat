package com.platform.monitor.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("monitor_sql_log")
public class MonitorSqlLog {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long configId;
    
    private Long systemId;
    
    private String sqlContent;
    
    private String params;
    
    private Integer executeTime;
    
    private Integer status;
    
    private String errorMessage;
    
    private LocalDateTime executeTime;
    
    private LocalDateTime createTime;
}

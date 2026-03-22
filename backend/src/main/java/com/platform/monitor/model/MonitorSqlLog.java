package com.platform.monitor.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

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
    private LocalDateTime createTime;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getConfigId() { return configId; }
    public void setConfigId(Long configId) { this.configId = configId; }
    public Long getSystemId() { return systemId; }
    public void setSystemId(Long systemId) { this.systemId = systemId; }
    public String getSqlContent() { return sqlContent; }
    public void setSqlContent(String sqlContent) { this.sqlContent = sqlContent; }
    public String getParams() { return params; }
    public void setParams(String params) { this.params = params; }
    public Integer getExecuteTime() { return executeTime; }
    public void setExecuteTime(Integer executeTime) { this.executeTime = executeTime; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}

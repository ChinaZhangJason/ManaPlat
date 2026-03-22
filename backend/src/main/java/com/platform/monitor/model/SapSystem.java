package com.platform.monitor.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

@TableName("sap_system")
public class SapSystem {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String systemName;
    private String host;
    private Integer port;
    private String databaseName;
    private String username;
    private String password;
    private String description;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSystemName() { return systemName; }
    public void setSystemName(String systemName) { this.systemName = systemName; }
    public String getHost() { return host; }
    public void setHost(String host) { this.host = host; }
    public Integer getPort() { return port; }
    public void setPort(Integer port) { this.port = port; }
    public String getDatabaseName() { return databaseName; }
    public void setDatabaseName(String databaseName) { this.databaseName = databaseName; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}

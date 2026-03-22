package com.platform.integrate.model;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;

@TableName("integrate_platform")
public class IntegratePlatform {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String code;
    private String description;
    private String baseUrl;
    private String ssoMode;
    private String ssoEndpoint;
    private String appKey;
    private String appSecret;
    private Integer status;
    private String allowedRoles;
    private String allowedPermissions;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableLogic
    private Integer deleted;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getBaseUrl() { return baseUrl; }
    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
    public String getSsoMode() { return ssoMode; }
    public void setSsoMode(String ssoMode) { this.ssoMode = ssoMode; }
    public String getSsoEndpoint() { return ssoEndpoint; }
    public void setSsoEndpoint(String ssoEndpoint) { this.ssoEndpoint = ssoEndpoint; }
    public String getAppKey() { return appKey; }
    public void setAppKey(String appKey) { this.appKey = appKey; }
    public String getAppSecret() { return appSecret; }
    public void setAppSecret(String appSecret) { this.appSecret = appSecret; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getAllowedRoles() { return allowedRoles; }
    public void setAllowedRoles(String allowedRoles) { this.allowedRoles = allowedRoles; }
    public String getAllowedPermissions() { return allowedPermissions; }
    public void setAllowedPermissions(String allowedPermissions) { this.allowedPermissions = allowedPermissions; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    public Integer getDeleted() { return deleted; }
    public void setDeleted(Integer deleted) { this.deleted = deleted; }
}

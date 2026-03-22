package com.platform.system.model;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;

@TableName("sys_permission")
public class SysPermission {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String permissionCode;
    private String permissionName;
    private String permissionType;
    private String menuPath;
    private Long parentId;
    private Integer sortOrder;
    private String icon;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @TableLogic
    private Integer deleted;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPermissionCode() { return permissionCode; }
    public void setPermissionCode(String permissionCode) { this.permissionCode = permissionCode; }
    public String getPermissionName() { return permissionName; }
    public void setPermissionName(String permissionName) { this.permissionName = permissionName; }
    public String getPermissionType() { return permissionType; }
    public void setPermissionType(String permissionType) { this.permissionType = permissionType; }
    public String getMenuPath() { return menuPath; }
    public void setMenuPath(String menuPath) { this.menuPath = menuPath; }
    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public Integer getDeleted() { return deleted; }
    public void setDeleted(Integer deleted) { this.deleted = deleted; }
}

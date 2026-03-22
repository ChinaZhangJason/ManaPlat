package com.platform.system.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
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
}

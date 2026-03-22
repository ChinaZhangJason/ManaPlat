package com.platform.integrate.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
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
}

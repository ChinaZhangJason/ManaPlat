package com.platform.monitor.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
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
}

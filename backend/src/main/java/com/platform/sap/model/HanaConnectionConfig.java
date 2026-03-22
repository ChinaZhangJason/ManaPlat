package com.platform.sap.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("hana_connection_config")
public class HanaConnectionConfig {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String configName;
    private String configCode;
    private Long systemId;
    private String host;
    private Integer port;
    private String databaseName;
    private String tenantName;
    private String username;
    private String password;
    private String connectionType;
    private Integer poolSize;
    private Integer poolMinIdle;
    private Integer connectionTimeout;
    private Integer socketTimeout;
    private Integer sslEnabled;
    private String otherConfig;
    private Integer status;
    private Long createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

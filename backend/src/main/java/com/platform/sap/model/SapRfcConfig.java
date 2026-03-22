package com.platform.sap.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("sap_rfc_config")
public class SapRfcConfig {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String configName;
    private String configCode;
    private Long systemId;
    private String connectionType;
    private String host;
    private Integer port;
    private String client;
    private String lang;
    private String systemNumber;
    private Integer poolSize;
    private String connectionConfig;
    private Integer status;
    private Long createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

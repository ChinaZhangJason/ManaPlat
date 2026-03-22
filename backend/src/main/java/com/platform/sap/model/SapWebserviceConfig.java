package com.platform.sap.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("sap_webservice_config")
public class SapWebserviceConfig {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String configName;
    private String configCode;
    private Long systemId;
    private String wsdlUrl;
    private String serviceName;
    private String portName;
    private String endpointUrl;
    private String authType;
    private String authConfig;
    private Integer status;
    private Long createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

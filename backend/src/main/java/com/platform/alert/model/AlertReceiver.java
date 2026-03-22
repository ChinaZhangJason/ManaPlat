package com.platform.alert.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("monitor_alert_receiver")
public class AlertReceiver {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String receiverName;
    
    private String receiverType;
    
    @TableField("user_id")
    private Long userId;
    
    private String notifyChannels;
    
    private String notifyConfig;
    
    private Integer status;
    
    @TableField("created_at")
    private LocalDateTime createdAt;
    
    @TableField("updated_at")
    private LocalDateTime updatedAt;
}

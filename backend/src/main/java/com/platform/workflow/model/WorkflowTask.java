package com.platform.workflow.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("workflow_task")
public class WorkflowTask {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("instance_id")
    private Long instanceId;
    
    private String taskKey;
    private String taskName;
    private Long assigneeId;
    private String taskState;
    private String comment;
    private String variables;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
}

package com.platform.workflow.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

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
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getInstanceId() { return instanceId; }
    public void setInstanceId(Long instanceId) { this.instanceId = instanceId; }
    public String getTaskKey() { return taskKey; }
    public void setTaskKey(String taskKey) { this.taskKey = taskKey; }
    public String getTaskName() { return taskName; }
    public void setTaskName(String taskName) { this.taskName = taskName; }
    public Long getAssigneeId() { return assigneeId; }
    public void setAssigneeId(Long assigneeId) { this.assigneeId = assigneeId; }
    public String getTaskState() { return taskState; }
    public void setTaskState(String taskState) { this.taskState = taskState; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public String getVariables() { return variables; }
    public void setVariables(String variables) { this.variables = variables; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
}

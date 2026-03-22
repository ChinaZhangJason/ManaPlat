package com.platform.workflow.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

@TableName("workflow_definition")
public class WorkflowDefinition {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String definitionName;
    private String definitionKey;
    private String description;
    private String formConfig;
    private String flowConfig;
    private Integer version;
    private Integer status;
    private Long createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDefinitionName() { return definitionName; }
    public void setDefinitionName(String definitionName) { this.definitionName = definitionName; }
    public String getDefinitionKey() { return definitionKey; }
    public void setDefinitionKey(String definitionKey) { this.definitionKey = definitionKey; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getFormConfig() { return formConfig; }
    public void setFormConfig(String formConfig) { this.formConfig = formConfig; }
    public String getFlowConfig() { return flowConfig; }
    public void setFlowConfig(String flowConfig) { this.flowConfig = flowConfig; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}

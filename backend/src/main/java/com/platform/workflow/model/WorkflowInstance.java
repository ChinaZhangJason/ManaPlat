package com.platform.workflow.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

@TableName("workflow_instance")
public class WorkflowInstance {
    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField("definition_id")
    private Long definitionId;
    private String instanceKey;
    private String businessKey;
    private String currentState;
    private String formData;
    private Long applicantId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime finishedAt;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getDefinitionId() { return definitionId; }
    public void setDefinitionId(Long definitionId) { this.definitionId = definitionId; }
    public String getInstanceKey() { return instanceKey; }
    public void setInstanceKey(String instanceKey) { this.instanceKey = instanceKey; }
    public String getBusinessKey() { return businessKey; }
    public void setBusinessKey(String businessKey) { this.businessKey = businessKey; }
    public String getCurrentState() { return currentState; }
    public void setCurrentState(String currentState) { this.currentState = currentState; }
    public String getFormData() { return formData; }
    public void setFormData(String formData) { this.formData = formData; }
    public Long getApplicantId() { return applicantId; }
    public void setApplicantId(Long applicantId) { this.applicantId = applicantId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public LocalDateTime getFinishedAt() { return finishedAt; }
    public void setFinishedAt(LocalDateTime finishedAt) { this.finishedAt = finishedAt; }
}

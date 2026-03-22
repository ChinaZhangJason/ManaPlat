package com.platform.workflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.workflow.model.WorkflowDefinition;
import com.platform.workflow.model.WorkflowInstance;
import com.platform.workflow.model.WorkflowTask;
import java.util.List;
import java.util.Map;

public interface WorkflowService extends IService<WorkflowDefinition> {
    WorkflowInstance startProcess(Long definitionId, Long applicantId, Map<String, Object> formData);
    List<WorkflowTask> getTasksByAssignee(Long assigneeId);
    void completeTask(Long taskId, Long userId, String action, String comment);
    List<WorkflowInstance> getInstancesByApplicant(Long applicantId);
    WorkflowInstance getInstance(Long instanceId);
}

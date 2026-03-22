package com.platform.workflow.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.workflow.mapper.WorkflowDefinitionMapper;
import com.platform.workflow.mapper.WorkflowInstanceMapper;
import com.platform.workflow.mapper.WorkflowTaskMapper;
import com.platform.workflow.model.WorkflowDefinition;
import com.platform.workflow.model.WorkflowInstance;
import com.platform.workflow.model.WorkflowTask;
import com.platform.workflow.service.WorkflowService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class WorkflowServiceImpl extends ServiceImpl<WorkflowDefinitionMapper, WorkflowDefinition> implements WorkflowService {

    private final WorkflowInstanceMapper instanceMapper;
    private final WorkflowTaskMapper taskMapper;

    public WorkflowServiceImpl(WorkflowInstanceMapper instanceMapper, WorkflowTaskMapper taskMapper) {
        this.instanceMapper = instanceMapper;
        this.taskMapper = taskMapper;
    }

    @Override
    @Transactional
    public WorkflowInstance startProcess(Long definitionId, Long applicantId, Map<String, Object> formData) {
        WorkflowDefinition definition = this.getById(definitionId);
        if (definition == null) {
            throw new RuntimeException("流程定义不存在");
        }

        WorkflowInstance instance = new WorkflowInstance();
        instance.setDefinitionId(definitionId);
        instance.setInstanceKey("INST-" + System.currentTimeMillis());
        instance.setCurrentState("DRAFT");
        instance.setFormData(JSONUtil.toJsonStr(formData));
        instance.setApplicantId(applicantId);
        instance.setCreatedAt(LocalDateTime.now());
        instance.setUpdatedAt(LocalDateTime.now());
        instanceMapper.insert(instance);

        String flowConfig = definition.getFlowConfig();
        Map<String, Object> config = JSONUtil.toBean(flowConfig, Map.class);
        String startState = (String) config.get("startState");

        instance.setCurrentState(startState);
        instanceMapper.updateById(instance);

        createTask(instance.getId(), startState, applicantId);

        return instance;
    }

    @Override
    public List<WorkflowTask> getTasksByAssignee(Long assigneeId) {
        return taskMapper.selectList(
            new LambdaQueryWrapper<WorkflowTask>()
                .eq(WorkflowTask::getAssigneeId, assigneeId)
                .eq(WorkflowTask::getTaskState, "PENDING")
                .orderByDesc(WorkflowTask::getCreatedAt)
        );
    }

    @Override
    @Transactional
    public void completeTask(Long taskId, Long userId, String action, String comment) {
        WorkflowTask task = taskMapper.selectById(taskId);
        if (task == null) {
            throw new RuntimeException("任务不存在");
        }

        task.setTaskState("COMPLETED");
        task.setComment(comment);
        task.setCompletedAt(LocalDateTime.now());
        taskMapper.updateById(task);

        WorkflowInstance instance = instanceMapper.selectById(task.getInstanceId());
        String nextState = getNextState(instance.getDefinitionId(), instance.getCurrentState(), action);
        
        if (nextState != null && !"END".equals(nextState)) {
            instance.setCurrentState(nextState);
            instanceMapper.updateById(instance);
            createTask(instance.getId(), nextState, userId);
        } else {
            instance.setCurrentState("COMPLETED");
            instance.setFinishedAt(LocalDateTime.now());
            instanceMapper.updateById(instance);
        }
    }

    @Override
    public List<WorkflowInstance> getInstancesByApplicant(Long applicantId) {
        return instanceMapper.selectList(
            new LambdaQueryWrapper<WorkflowInstance>()
                .eq(WorkflowInstance::getApplicantId, applicantId)
                .orderByDesc(WorkflowInstance::getCreatedAt)
        );
    }

    @Override
    public WorkflowInstance getInstance(Long instanceId) {
        return instanceMapper.selectById(instanceId);
    }

    private void createTask(Long instanceId, String state, Long assigneeId) {
        WorkflowTask task = new WorkflowTask();
        task.setInstanceId(instanceId);
        task.setTaskKey(state);
        task.setTaskName(state);
        task.setAssigneeId(assigneeId);
        task.setTaskState("PENDING");
        task.setCreatedAt(LocalDateTime.now());
        taskMapper.insert(task);
    }

    private String getNextState(Long definitionId, String currentState, String action) {
        return null;
    }
}

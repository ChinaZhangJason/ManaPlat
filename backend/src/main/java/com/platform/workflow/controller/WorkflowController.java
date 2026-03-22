package com.platform.workflow.controller;

import com.platform.common.Result;
import com.platform.workflow.model.WorkflowDefinition;
import com.platform.workflow.model.WorkflowInstance;
import com.platform.workflow.model.WorkflowTask;
import com.platform.workflow.service.WorkflowService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/workflow")
public class WorkflowController {

    private final WorkflowService workflowService;

    public WorkflowController(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    @GetMapping("/definition/list")
    public Result<List<WorkflowDefinition>> listDefinitions() {
        return Result.success(workflowService.list());
    }

    @GetMapping("/definition/{id}")
    public Result<WorkflowDefinition> getDefinition(@PathVariable Long id) {
        return Result.success(workflowService.getById(id));
    }

    @PostMapping("/definition")
    public Result<WorkflowDefinition> createDefinition(@RequestBody WorkflowDefinition definition) {
        workflowService.save(definition);
        return Result.success(definition);
    }

    @PutMapping("/definition/{id}")
    public Result<WorkflowDefinition> updateDefinition(@PathVariable Long id, @RequestBody WorkflowDefinition definition) {
        definition.setId(id);
        workflowService.updateById(definition);
        return Result.success(definition);
    }

    @PostMapping("/start")
    public Result<WorkflowInstance> startProcess(@RequestBody Map<String, Object> params) {
        Long definitionId = Long.valueOf(params.get("definitionId").toString());
        Long applicantId = Long.valueOf(params.get("applicantId").toString());
        @SuppressWarnings("unchecked")
        Map<String, Object> formData = (Map<String, Object>) params.get("formData");
        WorkflowInstance instance = workflowService.startProcess(definitionId, applicantId, formData);
        return Result.success(instance);
    }

    @GetMapping("/task/todo")
    public Result<List<WorkflowTask>> getTodoTasks(@RequestParam Long assigneeId) {
        return Result.success(workflowService.getTasksByAssignee(assigneeId));
    }

    @PostMapping("/task/{taskId}/complete")
    public Result<Void> completeTask(
            @PathVariable Long taskId,
            @RequestParam Long userId,
            @RequestParam(defaultValue = "APPROVE") String action,
            @RequestParam(required = false) String comment) {
        workflowService.completeTask(taskId, userId, action, comment);
        return Result.success();
    }

    @GetMapping("/instance/list")
    public Result<List<WorkflowInstance>> getMyInstances(@RequestParam Long applicantId) {
        return Result.success(workflowService.getInstancesByApplicant(applicantId));
    }

    @GetMapping("/instance/{id}")
    public Result<WorkflowInstance> getInstance(@PathVariable Long id) {
        return Result.success(workflowService.getInstance(id));
    }
}

package com.platform.workflow;

import com.platform.workflow.service.WorkflowService;
import com.platform.workflow.model.WorkflowDefinition;
import com.platform.workflow.model.WorkflowInstance;
import com.platform.workflow.model.WorkflowTask;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WorkflowServiceTest {

    @Autowired
    private WorkflowService workflowService;

    @Test
    void testListDefinitions() {
        List<WorkflowDefinition> definitions = workflowService.list();
        assertNotNull(definitions);
    }

    @Test
    void testGetTasksByAssignee() {
        List<WorkflowTask> tasks = workflowService.getTasksByAssignee(1L);
        assertNotNull(tasks);
    }

    @Test
    void testStartProcess() {
        WorkflowDefinition definition = workflowService.getById(1L);
        if (definition != null) {
            Map<String, Object> formData = new HashMap<>();
            formData.put("title", "测试申请");
            formData.put("content", "测试内容");

            WorkflowInstance instance = workflowService.startProcess(1L, 1L, formData);
            
            assertNotNull(instance);
            assertNotNull(instance.getId());
            assertNotNull(instance.getCurrentState());
        }
    }

    @Test
    void testGetInstancesByApplicant() {
        List<WorkflowInstance> instances = workflowService.getInstancesByApplicant(1L);
        assertNotNull(instances);
    }
}

package com.platform.workflow;

import com.platform.workflow.model.WorkflowDefinition;
import com.platform.workflow.model.WorkflowInstance;
import com.platform.workflow.model.WorkflowTask;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class WorkflowTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testWorkflowDefinitionCreation() throws Exception {
        WorkflowDefinition definition = new WorkflowDefinition();
        definition.setId(1L);
        definition.setDefinitionName("请假审批流程");
        definition.setDefinitionKey("LEAVE_APPROVAL");
        definition.setVersion(1);
        definition.setStatus(1);
        
        Map<String, Object> flowConfig = new HashMap<>();
        flowConfig.put("startState", "DRAFT");
        flowConfig.put("states", Arrays.asList("DRAFT", "PENDING_APPROVAL", "APPROVED", "REJECTED"));
        definition.setFlowConfig(objectMapper.writeValueAsString(flowConfig));
        
        assertEquals("请假审批流程", definition.getDefinitionName());
        assertEquals("LEAVE_APPROVAL", definition.getDefinitionKey());
        assertEquals(1, definition.getVersion());
    }

    @Test
    void testWorkflowInstanceCreation() {
        WorkflowInstance instance = new WorkflowInstance();
        instance.setId(1L);
        instance.setDefinitionId(1L);
        instance.setInstanceKey("INST-20240101-001");
        instance.setCurrentState("DRAFT");
        instance.setApplicantId(1L);
        
        assertEquals("DRAFT", instance.getCurrentState());
        assertEquals("INST-20240101-001", instance.getInstanceKey());
    }

    @Test
    void testWorkflowTaskCreation() {
        WorkflowTask task = new WorkflowTask();
        task.setId(1L);
        task.setInstanceId(1L);
        task.setTaskKey("APPROVAL");
        task.setTaskName("审批任务");
        task.setAssigneeId(2L);
        task.setTaskState("PENDING");
        
        assertEquals("PENDING", task.getTaskState());
        assertEquals(2L, task.getAssigneeId());
    }

    @Test
    void testStateTransitions() {
        Map<String, String[]> transitions = new HashMap<>();
        transitions.put("DRAFT", new String[]{"PENDING_APPROVAL"});
        transitions.put("PENDING_APPROVAL", new String[]{"APPROVED", "REJECTED"});
        transitions.put("APPROVED", new String[]{});
        transitions.put("REJECTED", new String[]{"DRAFT"});
        
        assertArrayEquals(new String[]{"PENDING_APPROVAL"}, transitions.get("DRAFT"));
        assertArrayEquals(new String[]{"APPROVED", "REJECTED"}, transitions.get("PENDING_APPROVAL"));
        assertEquals(0, transitions.get("APPROVED").length);
    }

    @Test
    void testWorkflowTaskCompletion() {
        WorkflowTask task = new WorkflowTask();
        task.setTaskState("PENDING");
        
        task.setTaskState("COMPLETED");
        task.setCompletedAt(LocalDateTime.now());
        
        assertEquals("COMPLETED", task.getTaskState());
        assertNotNull(task.getCompletedAt());
    }

    @Test
    void testInstanceStateChange() {
        WorkflowInstance instance = new WorkflowInstance();
        instance.setCurrentState("DRAFT");
        
        instance.setCurrentState("PENDING_APPROVAL");
        assertEquals("PENDING_APPROVAL", instance.getCurrentState());
        
        instance.setCurrentState("APPROVED");
        assertEquals("APPROVED", instance.getCurrentState());
        
        instance.setFinishedAt(LocalDateTime.now());
        assertNotNull(instance.getFinishedAt());
    }

    @Test
    void testWorkflowVariables() throws Exception {
        Map<String, Object> variables = new HashMap<>();
        variables.put("leaveDays", 5);
        variables.put("reason", "个人事务");
        variables.put("approver", "张三");
        
        WorkflowTask task = new WorkflowTask();
        task.setVariables(objectMapper.writeValueAsString(variables));
        
        assertNotNull(task.getVariables());
        assertTrue(task.getVariables().contains("leaveDays"));
    }
}

package com.platform.workflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.platform.workflow.model.WorkflowTask;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WorkflowTaskMapper extends BaseMapper<WorkflowTask> {
}

package com.platform.monitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.platform.monitor.model.MonitorSqlLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SqlLogMapper extends BaseMapper<MonitorSqlLog> {
}

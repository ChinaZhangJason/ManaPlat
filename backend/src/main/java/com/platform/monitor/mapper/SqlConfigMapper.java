package com.platform.monitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.platform.monitor.model.MonitorSqlConfig;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SqlConfigMapper extends BaseMapper<MonitorSqlConfig> {
}

package com.platform.monitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.platform.monitor.mapper.SqlConfigMapper;
import com.platform.monitor.model.MonitorSqlConfig;
import com.platform.monitor.service.SqlConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SqlConfigServiceImpl implements SqlConfigService {

    @Autowired
    private SqlConfigMapper sqlConfigMapper;

    @Override
    public List<MonitorSqlConfig> listAll() {
        return sqlConfigMapper.selectList(null);
    }

    @Override
    public MonitorSqlConfig getById(Long id) {
        return sqlConfigMapper.selectById(id);
    }

    @Override
    public List<MonitorSqlConfig> getBySystemId(Long systemId) {
        LambdaQueryWrapper<MonitorSqlConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MonitorSqlConfig::getSystemId, systemId);
        return sqlConfigMapper.selectList(wrapper);
    }

    @Override
    public boolean save(MonitorSqlConfig config) {
        config.setCreateTime(LocalDateTime.now());
        config.setUpdateTime(LocalDateTime.now());
        return sqlConfigMapper.insert(config) > 0;
    }

    @Override
    public boolean update(MonitorSqlConfig config) {
        config.setUpdateTime(LocalDateTime.now());
        return sqlConfigMapper.updateById(config) > 0;
    }

    @Override
    public boolean deleteById(Long id) {
        return sqlConfigMapper.deleteById(id) > 0;
    }
}

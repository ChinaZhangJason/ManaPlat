package com.platform.monitor.service.impl;

import com.platform.monitor.mapper.SapSystemMapper;
import com.platform.monitor.model.SapSystem;
import com.platform.monitor.service.HanaQueryService;
import com.platform.monitor.service.SapSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SapSystemServiceImpl implements SapSystemService {

    @Autowired
    private SapSystemMapper sapSystemMapper;
    
    @Autowired
    private HanaQueryService hanaQueryService;

    @Override
    public List<SapSystem> listAll() {
        return sapSystemMapper.selectList(null);
    }

    @Override
    public SapSystem getById(Long id) {
        return sapSystemMapper.selectById(id);
    }

    @Override
    public boolean save(SapSystem system) {
        system.setCreateTime(LocalDateTime.now());
        system.setUpdateTime(LocalDateTime.now());
        return sapSystemMapper.insert(system) > 0;
    }

    @Override
    public boolean update(SapSystem system) {
        system.setUpdateTime(LocalDateTime.now());
        return sapSystemMapper.updateById(system) > 0;
    }

    @Override
    public boolean deleteById(Long id) {
        return sapSystemMapper.deleteById(id) > 0;
    }

    @Override
    public boolean testConnection(Long id) {
        return hanaQueryService.testConnection(id);
    }
}

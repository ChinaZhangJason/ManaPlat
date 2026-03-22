package com.platform.monitor.service;

import com.platform.monitor.model.SapSystem;

import java.util.List;

public interface SapSystemService {
    List<SapSystem> listAll();
    
    SapSystem getById(Long id);
    
    boolean save(SapSystem system);
    
    boolean update(SapSystem system);
    
    boolean deleteById(Long id);
    
    boolean testConnection(Long id);
}

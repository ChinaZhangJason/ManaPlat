package com.platform.integrate.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.integrate.model.IntegratePlatform;

public interface PlatformService extends IService<IntegratePlatform> {
    IPage<IntegratePlatform> page(Integer current, Integer size, String name, String code, Integer status);
    
    IntegratePlatform getByCode(String code);
    
    boolean addPlatform(IntegratePlatform platform);
    
    boolean updatePlatform(IntegratePlatform platform);
    
    boolean deletePlatform(Long id);
    
    boolean hasAccess(Long userId, String platformCode);
}

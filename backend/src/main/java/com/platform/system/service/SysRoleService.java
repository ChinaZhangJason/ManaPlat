package com.platform.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.system.model.SysRole;

public interface SysRoleService extends IService<SysRole> {
    IPage<SysRole> page(Integer current, Integer size, String roleName, Integer status);
    
    boolean addRole(SysRole role);
    
    boolean updateRole(SysRole role);
    
    boolean deleteRole(Long id);
}

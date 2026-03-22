package com.platform.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.system.model.SysUser;

public interface SysUserService extends IService<SysUser> {
    IPage<SysUser> page(Integer current, Integer size, String username, Integer status);
    
    SysUser getByUsername(String username);
    
    boolean addUser(SysUser user);
    
    boolean updateUser(SysUser user);
    
    boolean deleteUser(Long id);
}

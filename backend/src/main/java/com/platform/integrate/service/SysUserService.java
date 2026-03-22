package com.platform.integrate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.system.model.SysUser;

public interface SysUserService extends IService<SysUser> {
    SysUser getById(Long id);
}

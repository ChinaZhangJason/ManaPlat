package com.platform.integrate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.system.model.SysRole;

import java.util.List;

public interface SysRoleService extends IService<SysRole> {
    List<SysRole> getByUserId(Long userId);
}

package com.platform.integrate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.system.model.SysPermission;

import java.util.List;

public interface SysPermissionService extends IService<SysPermission> {
    List<SysPermission> getByUserId(Long userId);
}

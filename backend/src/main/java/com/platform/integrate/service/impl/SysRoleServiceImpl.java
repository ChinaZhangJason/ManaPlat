package com.platform.integrate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.integrate.mapper.SysRoleMapper;
import com.platform.integrate.service.SysRoleService;
import com.platform.system.model.SysRole;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Override
    public List<SysRole> getByUserId(Long userId) {
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.inSql(SysRole::getId, 
                "SELECT role_id FROM sys_user_role WHERE user_id = " + userId);
        return list(wrapper);
    }
    
    public String getRoleCode(SysRole role) {
        return role.getRoleCode();
    }
}

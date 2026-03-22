package com.platform.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.system.mapper.SysRoleMapper;
import com.platform.system.model.SysRole;
import com.platform.system.service.SysRoleService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.List;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
    
    @Override
    public IPage<SysRole> page(Integer current, Integer size, String roleName, Integer status) {
        Page<SysRole> page = new Page<>(current, size);
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(roleName)) {
            wrapper.like(SysRole::getRoleName, roleName);
        }
        if (status != null) {
            wrapper.eq(SysRole::getStatus, status);
        }
        wrapper.orderByDesc(SysRole::getCreatedAt);
        return page(page, wrapper);
    }
    
    @Override
    public boolean addRole(SysRole role) {
        return save(role);
    }
    
    @Override
    public boolean updateRole(SysRole role) {
        return updateById(role);
    }
    
    @Override
    public boolean deleteRole(Long id) {
        return removeById(id);
    }
    
    @Override
    public List<SysRole> getByUserId(Long userId) {
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.inSql(SysRole::getId, 
                "SELECT role_id FROM sys_user_role WHERE user_id = " + userId);
        return list(wrapper);
    }
}

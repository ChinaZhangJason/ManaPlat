package com.platform.integrate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.integrate.mapper.SysPermissionMapper;
import com.platform.integrate.service.SysPermissionService;
import com.platform.system.model.SysPermission;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements SysPermissionService {

    @Override
    public List<SysPermission> getByUserId(Long userId) {
        LambdaQueryWrapper<SysPermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.inSql(SysPermission::getId,
                "SELECT DISTINCT p.id FROM sys_permission p " +
                "LEFT JOIN sys_role_permission rp ON p.id = rp.permission_id " +
                "LEFT JOIN sys_user_role ur ON rp.role_id = ur.role_id " +
                "WHERE ur.user_id = " + userId + " OR p.parent_id IN (" +
                "SELECT DISTINCT p2.id FROM sys_permission p2 " +
                "LEFT JOIN sys_role_permission rp2 ON p2.id = rp2.permission_id " +
                "LEFT JOIN sys_user_role ur2 ON rp2.role_id = ur2.role_id " +
                "WHERE ur2.user_id = " + userId + ")");
        List<SysPermission> permissions = list(wrapper);
        
        Set<SysPermission> result = new HashSet<>(permissions);
        for (SysPermission permission : permissions) {
            if (permission.getParentId() != null && permission.getParentId() > 0) {
                LambdaQueryWrapper<SysPermission> parentWrapper = new LambdaQueryWrapper<>();
                parentWrapper.eq(SysPermission::getId, permission.getParentId());
                SysPermission parent = getOne(parentWrapper);
                if (parent != null) {
                    result.add(parent);
                }
            }
        }
        
        return result.stream().collect(Collectors.toList());
    }
}

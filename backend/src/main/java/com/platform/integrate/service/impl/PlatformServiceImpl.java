package com.platform.integrate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.integrate.mapper.IntegratePlatformMapper;
import com.platform.integrate.model.IntegratePlatform;
import com.platform.integrate.service.PlatformService;
import com.platform.system.service.SysRoleService;
import com.platform.integrate.service.SysPermissionService;
import com.platform.system.service.SysUserService;
import com.platform.system.model.SysUser;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlatformServiceImpl extends ServiceImpl<IntegratePlatformMapper, IntegratePlatform> implements PlatformService {

    private final SysUserService sysUserService;
    private final SysRoleService sysRoleService;
    private final SysPermissionService sysPermissionService;

    public PlatformServiceImpl(SysUserService sysUserService, 
                               SysRoleService sysRoleService,
                               SysPermissionService sysPermissionService) {
        this.sysUserService = sysUserService;
        this.sysRoleService = sysRoleService;
        this.sysPermissionService = sysPermissionService;
    }

    @Override
    public IPage<IntegratePlatform> page(Integer current, Integer size, String name, String code, Integer status) {
        Page<IntegratePlatform> page = new Page<>(current, size);
        LambdaQueryWrapper<IntegratePlatform> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(name)) {
            wrapper.like(IntegratePlatform::getName, name);
        }
        if (StringUtils.hasText(code)) {
            wrapper.eq(IntegratePlatform::getCode, code);
        }
        if (status != null) {
            wrapper.eq(IntegratePlatform::getStatus, status);
        }
        wrapper.orderByDesc(IntegratePlatform::getCreateTime);
        return page(page, wrapper);
    }

    @Override
    public IntegratePlatform getByCode(String code) {
        LambdaQueryWrapper<IntegratePlatform> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegratePlatform::getCode, code);
        return getOne(wrapper);
    }

    @Override
    public boolean addPlatform(IntegratePlatform platform) {
        return save(platform);
    }

    @Override
    public boolean updatePlatform(IntegratePlatform platform) {
        return updateById(platform);
    }

    @Override
    public boolean deletePlatform(Long id) {
        return removeById(id);
    }

    @Override
    public boolean hasAccess(Long userId, String platformCode) {
        IntegratePlatform platform = getByCode(platformCode);
        if (platform == null || platform.getStatus() != 1) {
            return false;
        }

        SysUser user = sysUserService.getById(userId);
        if (user == null || user.getStatus() != 1) {
            return false;
        }

        String allowedRoles = platform.getAllowedRoles();
        String allowedPermissions = platform.getAllowedPermissions();

        if (!StringUtils.hasText(allowedRoles) && !StringUtils.hasText(allowedPermissions)) {
            return true;
        }

        if (StringUtils.hasText(allowedRoles)) {
            List<String> roleCodes = Arrays.stream(allowedRoles.split(","))
                    .map(String::trim)
                    .collect(Collectors.toList());
            
            List<com.platform.system.model.SysRole> userRoles = sysRoleService.getByUserId(userId);
            boolean hasRole = userRoles.stream()
                    .anyMatch(role -> roleCodes.contains(role.getRoleCode()));
            if (hasRole) {
                return true;
            }
        }

        if (StringUtils.hasText(allowedPermissions)) {
            List<String> permissionCodes = Arrays.stream(allowedPermissions.split(","))
                    .map(String::trim)
                    .collect(Collectors.toList());
            
            List<com.platform.system.model.SysPermission> userPermissions = sysPermissionService.getByUserId(userId);
            boolean hasPermission = userPermissions.stream()
                    .anyMatch(permission -> permissionCodes.contains(permission.getPermissionCode()));
            if (hasPermission) {
                return true;
            }
        }

        return false;
    }
}

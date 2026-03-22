package com.platform.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.platform.common.PageResult;
import com.platform.common.Result;
import com.platform.system.model.SysRole;
import com.platform.system.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/system/role")
public class SysRoleController {
    
    @Autowired
    private SysRoleService sysRoleService;
    
    @GetMapping("/page")
    public Result<PageResult<SysRole>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String roleName,
            @RequestParam(required = false) Integer status) {
        IPage<SysRole> page = sysRoleService.page(current, size, roleName, status);
        PageResult<SysRole> result = new PageResult<>(
                page.getRecords(), page.getTotal(), page.getSize(), page.getCurrent());
        return Result.success(result);
    }
    
    @GetMapping("/{id}")
    public Result<SysRole> getById(@PathVariable Long id) {
        return Result.success(sysRoleService.getById(id));
    }
    
    @GetMapping("/list")
    public Result<List<SysRole>> list() {
        return Result.success(sysRoleService.list());
    }
    
    @PostMapping
    public Result<Boolean> add(@RequestBody SysRole role) {
        return Result.success(sysRoleService.addRole(role));
    }
    
    @PutMapping
    public Result<Boolean> update(@RequestBody SysRole role) {
        return Result.success(sysRoleService.updateRole(role));
    }
    
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(sysRoleService.deleteRole(id));
    }
}

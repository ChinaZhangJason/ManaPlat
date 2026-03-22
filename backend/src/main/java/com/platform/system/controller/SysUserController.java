package com.platform.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.platform.common.PageResult;
import com.platform.common.Result;
import com.platform.system.model.SysUser;
import com.platform.system.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/system/user")
public class SysUserController {
    
    @Autowired
    private SysUserService sysUserService;
    
    @GetMapping("/page")
    public Result<PageResult<SysUser>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Integer status) {
        IPage<SysUser> page = sysUserService.page(current, size, username, status);
        PageResult<SysUser> result = new PageResult<>(
                page.getRecords(), page.getTotal(), page.getSize(), page.getCurrent());
        return Result.success(result);
    }
    
    @GetMapping("/{id}")
    public Result<SysUser> getById(@PathVariable Long id) {
        return Result.success(sysUserService.getById(id));
    }
    
    @GetMapping("/username/{username}")
    public Result<SysUser> getByUsername(@PathVariable String username) {
        return Result.success(sysUserService.getByUsername(username));
    }
    
    @GetMapping("/list")
    public Result<List<SysUser>> list() {
        return Result.success(sysUserService.list());
    }
    
    @PostMapping
    public Result<Boolean> add(@RequestBody SysUser user) {
        return Result.success(sysUserService.addUser(user));
    }
    
    @PutMapping
    public Result<Boolean> update(@RequestBody SysUser user) {
        return Result.success(sysUserService.updateUser(user));
    }
    
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(sysUserService.deleteUser(id));
    }
}

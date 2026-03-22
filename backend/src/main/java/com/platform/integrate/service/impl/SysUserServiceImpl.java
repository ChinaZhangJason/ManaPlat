package com.platform.integrate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.integrate.mapper.SysUserMapper;
import com.platform.integrate.service.SysUserService;
import com.platform.system.model.SysUser;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Override
    public SysUser getById(Long id) {
        return super.getById(id);
    }
}

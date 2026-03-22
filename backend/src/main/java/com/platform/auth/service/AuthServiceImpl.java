package com.platform.auth.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.platform.auth.dto.AuthResponse;
import com.platform.auth.dto.AuthResponse.UserInfo;
import com.platform.auth.dto.LoginRequest;
import com.platform.auth.dto.RegisterRequest;
import com.platform.auth.dto.ResetPasswordRequest;
import com.platform.auth.dto.SendCodeRequest;
import com.platform.auth.security.CustomUserDetails;
import com.platform.auth.security.JwtTokenProvider;
import com.platform.system.model.SysUser;
import com.platform.system.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final SysUserMapper sysUserMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String token = jwtTokenProvider.generateToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails.getUsername());

        return buildAuthResponse(userDetails, token, refreshToken);
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, request.getUsername());
        if (sysUserMapper.selectCount(wrapper) > 0) {
            throw new RuntimeException("用户名已存在");
        }

        SysUser user = new SysUser();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setStatus(1);
        sysUserMapper.insert(user);

        CustomUserDetails userDetails = new CustomUserDetails(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getPhone(),
                Collections.singletonList("USER"),
                true
        );

        String token = jwtTokenProvider.generateToken(userDetails.getUsername());
        String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails.getUsername());

        return buildAuthResponse(userDetails, token, refreshToken);
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken) || !jwtTokenProvider.isRefreshToken(refreshToken)) {
            throw new RuntimeException("无效的刷新令牌");
        }

        String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
        String newToken = jwtTokenProvider.generateToken(username);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(username);

        UserInfo userInfo = getUserInfo(username);
        return AuthResponse.builder()
                .token(newToken)
                .refreshToken(newRefreshToken)
                .userInfo(userInfo)
                .build();
    }

    @Override
    public UserInfo getUserInfo(String username) {
        SysUser user = sysUserMapper.selectOne(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username)
        );

        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        return UserInfo.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
    }

    private AuthResponse buildAuthResponse(CustomUserDetails userDetails, String token, String refreshToken) {
        UserInfo userInfo = UserInfo.builder()
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .phone(userDetails.getPhone())
                .build();

        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .userInfo(userInfo)
                .build();
    }

    @Override
    public void sendCode(SendCodeRequest request) {
        String code = String.valueOf((int) ((Math.random() * 900000) + 100000));
        String target = request.getTarget();
        String scene = request.getScene() != null ? request.getScene() : "RESET_PASSWORD";

        System.out.println("[VERIFY_CODE] Code: " + code + ", Target: " + target + ", Scene: " + scene);

        if ("SMS".equalsIgnoreCase(request.getType())) {
            System.out.println("[SMS] Sending code " + code + " to " + target);
        } else if ("EMAIL".equalsIgnoreCase(request.getType())) {
            System.out.println("[EMAIL] Sending code " + code + " to " + target);
        }
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        SysUser user = sysUserMapper.selectOne(
            new LambdaQueryWrapper<SysUser>()
                .eq("phone".equals(request.getType()), SysUser::getPhone, request.getTarget())
                .eq("email".equals(request.getType()), SysUser::getEmail, request.getTarget())
        );

        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        sysUserMapper.updateById(user);
    }
}

package com.platform.auth.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.platform.auth.dto.ResetPasswordRequest;
import com.platform.auth.dto.SendCodeRequest;
import com.platform.auth.service.VerifyCodeService;
import com.platform.common.BusinessException;
import com.platform.system.mapper.SysUserMapper;
import com.platform.system.model.SysUser;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class VerifyCodeServiceImpl implements VerifyCodeService {

    private final SysUserMapper userMapper;
    private final JdbcTemplate jdbcTemplate;
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:noreply@example.com}")
    private String fromEmail;

    public VerifyCodeServiceImpl(SysUserMapper userMapper, JdbcTemplate jdbcTemplate, JavaMailSender mailSender) {
        this.userMapper = userMapper;
        this.jdbcTemplate = jdbcTemplate;
        this.mailSender = mailSender;
    }

    @Override
    public void sendCode(SendCodeRequest request) {
        String code = RandomUtil.randomNumbers(6);
        String target = request.getTarget();
        String scene = request.getScene() != null ? request.getScene() : "RESET_PASSWORD";
        
        String sql = "INSERT INTO verify_code (code_type, target, code, scene, expire_time, created_at) VALUES (?, ?, ?, ?, ?, NOW())";
        jdbcTemplate.update(sql, request.getType().toUpperCase(), target, code, scene, LocalDateTime.now().plusMinutes(10));

        if ("SMS".equalsIgnoreCase(request.getType())) {
            sendSms(target, code);
        } else if ("EMAIL".equalsIgnoreCase(request.getType())) {
            sendEmail(target, code);
        }
    }

    @Override
    public void verifyCode(String type, String target, String code) {
        String sql = "SELECT * FROM verify_code WHERE code_type = ? AND target = ? AND code = ? AND expire_time > NOW() AND used = 0 ORDER BY created_at DESC LIMIT 1";
        var results = jdbcTemplate.queryForList(sql, type.toUpperCase(), target, code);
        
        if (results.isEmpty()) {
            throw new BusinessException("验证码无效或已过期");
        }
        
        jdbcTemplate.update("UPDATE verify_code SET used = 1 WHERE id = ?", results.get(0).get("id"));
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        verifyCode(request.getType(), request.getTarget(), request.getCode());
        
        String encodedPassword = "{noop}" + request.getNewPassword();
        String sql = "UPDATE sys_user SET password = ? WHERE " + 
            ("phone".equals(request.getType()) ? "phone" : "email") + " = ?";
        int updated = jdbcTemplate.update(sql, encodedPassword, request.getTarget());
        
        if (updated == 0) {
            throw new BusinessException("用户不存在");
        }
    }

    private void sendSms(String phone, String code) {
        System.out.println("[SMS] Sending code " + code + " to " + phone);
    }

    private void sendEmail(String email, String code) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(email);
            message.setSubject("密码重置验证码");
            message.setText("您的验证码是: " + code + "，10分钟内有效。");
            mailSender.send(message);
        } catch (Exception e) {
            throw new BusinessException("邮件发送失败: " + e.getMessage());
        }
    }
}

package com.platform.auth.service;

import com.platform.auth.dto.ResetPasswordRequest;
import com.platform.auth.dto.SendCodeRequest;

public interface VerifyCodeService {
    void sendCode(SendCodeRequest request);
    void verifyCode(String type, String target, String code);
    void resetPassword(ResetPasswordRequest request);
}

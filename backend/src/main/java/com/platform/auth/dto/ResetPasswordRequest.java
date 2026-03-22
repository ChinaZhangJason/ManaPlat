package com.platform.auth.dto;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String type;
    private String target;
    private String code;
    private String newPassword;
}

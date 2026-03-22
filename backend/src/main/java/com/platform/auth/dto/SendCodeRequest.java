package com.platform.auth.dto;

import lombok.Data;

@Data
public class SendCodeRequest {
    private String type;
    private String target;
    private String scene;
}

package com.platform.auth.dto;

public class SendCodeRequest {
    private String type;
    private String target;
    private String scene;
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getTarget() { return target; }
    public void setTarget(String target) { this.target = target; }
    public String getScene() { return scene; }
    public void setScene(String scene) { this.scene = scene; }
}

package com.example.System.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
@Schema(description = "登录请求")
public class LoginRequest {
    
    @Schema(description = "用户名", example = "admin")
    @NotBlank(message = "用户名不能为空")
    private String username;
    
    @Schema(description = "密码", example = "123456")
    @NotBlank(message = "密码不能为空")
    private String password;
    
    // 手动添加getter方法以确保编译成功
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
}
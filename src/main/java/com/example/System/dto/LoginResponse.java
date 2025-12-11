package com.example.System.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "登录响应")
public class LoginResponse {
    
    @Schema(description = "JWT令牌", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInVzZXJJZCI6MSwicm9sZSI6IkFETUlOIiwiaWF0IjoxNjM5NTY4NDAwLCJleHAiOjE2Mzk2NTQ4MDB9.abc123def456")
    private String token;
    
    @Schema(description = "用户ID", example = "1")
    private Long userId;
    
    @Schema(description = "用户名", example = "admin")
    private String username;
    
    @Schema(description = "昵称", example = "管理员")
    private String nickname;
    
    @Schema(description = "用户角色", example = "ADMIN")
    private String role;
    
    @Schema(description = "头像URL", example = "https://example.com/avatar.jpg")
    private String avatar;
    
    @Schema(description = "用户权限代码列表", example = "user:manage,role:manage,permission:view")
    private String permissions;
    
    public static LoginResponse build(String token, Long userId, String username, String nickname, String role, String avatar) {
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUserId(userId);
        response.setUsername(username);
        response.setNickname(nickname);
        response.setRole(role);
        response.setAvatar(avatar);
        return response;
    }
    
    public static LoginResponse build(String token, Long userId, String username, String nickname, String role, String avatar, String permissions) {
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUserId(userId);
        response.setUsername(username);
        response.setNickname(nickname);
        response.setRole(role);
        response.setAvatar(avatar);
        response.setPermissions(permissions);
        return response;
    }
    
    // 手动添加getter和setter方法以确保编译成功
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getNickname() {
        return nickname;
    }
    
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public String getAvatar() {
        return avatar;
    }
    
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    
    public String getPermissions() {
        return permissions;
    }
    
    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }
}
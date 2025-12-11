package com.example.System.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user")
@Schema(description = "用户实体")
public class User {

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "用户ID", example = "1")
    private Long id;

    @TableField("username")
    @Schema(description = "用户名", example = "admin")
    private String username;

    @TableField("password")
    @Schema(description = "密码", example = "123456")
    private String password;

    @TableField("email")
    @Schema(description = "邮箱", example = "admin@example.com")
    private String email;

    @TableField("phone")
    @Schema(description = "手机号", example = "13800138000")
    private String phone;

    @TableField("nickname")
    @Schema(description = "昵称", example = "管理员")
    private String nickname;

    @TableField("avatar")
    @Schema(description = "头像URL", example = "https://example.com/avatar.jpg")
    private String avatar;

    @TableField("role")
    @Schema(description = "用户角色", example = "ADMIN")
    private String role;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @Schema(description = "创建时间", example = "2023-01-01T12:00:00")
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间", example = "2023-01-01T12:00:00")
    private LocalDateTime updateTime;

    @TableLogic
    @TableField("deleted")
    @Schema(description = "是否删除：0-未删除，1-已删除", example = "0")
    private Integer deleted;
    
    // 手动添加getter和setter方法以确保编译成功
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
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
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getNickname() {
        return nickname;
    }
    
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    
    public String getAvatar() {
        return avatar;
    }
    
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
    
    public Integer getDeleted() {
        return deleted;
    }
    
    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }
}
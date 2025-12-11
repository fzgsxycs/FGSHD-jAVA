package com.example.System.controller;

import com.example.System.annotation.RequirePermission;
import com.example.System.common.Result;
import com.example.System.entity.User;
import com.example.System.exception.BusinessException;
import com.example.System.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Tag(name = "用户管理", description = "用户管理相关接口")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "获取用户列表")
    @GetMapping("/list")
    @RequirePermission(value = {"user:view"})
    public Result<List<User>> getAllUsers() {
        List<User> users = userService.list();
        // 不返回密码
        users.forEach(user -> user.setPassword(null));
        return Result.success(users);
    }

    @Operation(summary = "获取用户详情")
    @GetMapping("/detail/{id}")
    @RequirePermission(value = {"user:view"})
    public Result<User> getUserDetail(@Parameter(description = "用户ID") @PathVariable Long id) {
        User user = userService.getById(id);
        if (user != null) {
            user.setPassword(null);
            return Result.success(user);
        } else {
            throw BusinessException.userNotFound();
        }
    }

    @Operation(summary = "创建用户")
    @PostMapping("/create")
    @RequirePermission(value = {"user:create"}, roles = {"admin"})
    public Result<String> createUser(@RequestBody User user) {
        boolean success = userService.register(user);
        if (success) {
            return Result.success("用户创建成功");
        } else {
            throw BusinessException.dataSaveFailed();
        }
    }

    @Operation(summary = "更新用户")
    @PutMapping("/update/{id}")
    @RequirePermission(value = {"user:update"})
    public Result<String> updateUser(@Parameter(description = "用户ID") @PathVariable Long id, @RequestBody User user) {
        User existingUser = userService.getById(id);
        if (existingUser == null) {
            throw BusinessException.userNotFound();
        }
        
        user.setId(id);
        boolean success = userService.updateById(user);
        if (success) {
            return Result.success("用户更新成功");
        } else {
            throw BusinessException.dataUpdateFailed();
        }
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/delete/{id}")
    @RequirePermission(value = {"user:delete"}, roles = {"admin"})
    public Result<String> deleteUser(@Parameter(description = "用户ID") @PathVariable Long id) {
        User existingUser = userService.getById(id);
        if (existingUser == null) {
            throw BusinessException.userNotFound();
        }
        
        boolean success = userService.removeById(id);
        if (success) {
            return Result.success("用户删除成功");
        } else {
            throw BusinessException.dataDeleteFailed();
        }
    }
}
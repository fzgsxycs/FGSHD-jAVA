package com.example.System.controller;

import com.example.System.common.Result;
import com.example.System.dto.LoginRequest;
import com.example.System.dto.LoginResponse;
import com.example.System.entity.User;
import com.example.System.exception.BusinessException;
import com.example.System.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "认证管理", description = "用户登录、注册和认证相关接口")
public class AuthController {

    @Autowired
    private UserService userService;

    @Operation(summary = "用户登录", description = "根据用户名和密码进行身份验证，成功后返回JWT令牌")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "登录成功", content = @Content(schema = @Schema(implementation = Result.class))),
            @ApiResponse(responseCode = "500", description = "登录失败", content = @Content(schema = @Schema(implementation = Result.class)))
    })
    @PostMapping("/login")
    public Result<LoginResponse> login(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "登录请求信息", required = true, content = @Content(schema = @Schema(implementation = LoginRequest.class))
    ) @RequestBody LoginRequest loginRequest) {
        LoginResponse response = userService.login(loginRequest);
        return Result.success(response);
    }

    @Operation(summary = "用户注册", description = "创建新用户账户")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "注册成功", content = @Content(schema = @Schema(implementation = Result.class))),
            @ApiResponse(responseCode = "500", description = "注册失败", content = @Content(schema = @Schema(implementation = Result.class)))
    })
    @PostMapping("/register")
    public Result<String> register(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "用户注册信息", required = true, content = @Content(schema = @Schema(implementation = User.class))
    ) @RequestBody User user) {
        boolean success = userService.register(user);
        if (success) {
            return Result.success("注册成功");
        } else {
            throw BusinessException.dataSaveFailed();
        }
    }

    @Operation(summary = "获取用户信息", description = "根据用户ID获取用户详细信息", security = @SecurityRequirement(name = "JWT"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "获取成功", content = @Content(schema = @Schema(implementation = Result.class))),
            @ApiResponse(responseCode = "500", description = "获取失败", content = @Content(schema = @Schema(implementation = Result.class)))
    })
    @GetMapping("/info")
    public Result<User> getUserInfo(@Parameter(description = "用户ID", required = true) @RequestParam Long userId) {
        User user = userService.getById(userId);
        if (user != null) {
            // 不返回密码
            user.setPassword(null);
            return Result.success(user);
        } else {
            throw BusinessException.userNotFound();
        }
    }
}
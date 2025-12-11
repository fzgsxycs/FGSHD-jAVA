package com.example.System.service.impl;

import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.System.dto.LoginRequest;
import com.example.System.dto.LoginResponse;
import com.example.System.entity.Permission;
import com.example.System.entity.User;
import com.example.System.exception.AuthException;
import com.example.System.exception.BusinessException;
import com.example.System.mapper.UserMapper;
import com.example.System.service.PermissionService;
import com.example.System.service.UserService;
import com.example.System.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private PermissionService permissionService;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        // 根据用户名查询用户
        User user = findByUsername(loginRequest.getUsername());
        if (user == null) {
            throw AuthException.userNotFound();
        }
        
        // 验证密码
        if (!BCrypt.checkpw(loginRequest.getPassword(), user.getPassword())) {
            throw AuthException.passwordError();
        }
        
        // 获取用户权限列表
        List<Permission> permissions = permissionService.getPermissionsByUserId(user.getId());
        String permissionCodes = permissions.stream()
                .map(Permission::getPermissionCode)
                .collect(Collectors.joining(","));
        
        // 生成JWT token（包含权限信息）
        String token = jwtUtil.generateToken(user.getUsername(), user.getId(), user.getRole(), permissionCodes);
        
        // 返回登录响应（包含权限信息）
        return LoginResponse.build(token, user.getId(), user.getUsername(), 
                                  user.getNickname(), user.getRole(), user.getAvatar(), permissionCodes);
    }

    @Override
    public User findByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return getOne(queryWrapper);
    }

    @Override
    public boolean register(User user) {
        // 检查用户名是否已存在
        if (findByUsername(user.getUsername()) != null) {
            throw BusinessException.userAlreadyExists();
        }
        
        // 加密密码
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        
        // 设置默认角色
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("USER");
        }
        
        return save(user);
    }
}
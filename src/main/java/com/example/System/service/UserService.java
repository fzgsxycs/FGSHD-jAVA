package com.example.System.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.System.dto.LoginRequest;
import com.example.System.dto.LoginResponse;
import com.example.System.entity.User;

public interface UserService extends IService<User> {
    
    /**
     * 用户登录
     */
    LoginResponse login(LoginRequest loginRequest);
    
    /**
     * 根据用户名查找用户
     */
    User findByUsername(String username);
    
    /**
     * 用户注册
     */
    boolean register(User user);
}
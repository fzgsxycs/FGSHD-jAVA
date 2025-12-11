package com.example.System.service.impl;

import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.System.entity.Role;
import com.example.System.entity.User;
import com.example.System.entity.UserRole;
import com.example.System.service.AdminInitService;
import com.example.System.service.RoleService;
import com.example.System.service.UserRoleService;
import com.example.System.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 管理员初始化服务实现类
 */
@Service
public class AdminInitServiceImpl implements AdminInitService {
    
    private static final Logger logger = LoggerFactory.getLogger(AdminInitServiceImpl.class);
    
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";
    private static final String ADMIN_EMAIL = "admin@example.com";
    private static final String ADMIN_NICKNAME = "系统管理员";
    private static final String ADMIN_ROLE_CODE = "admin";
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private RoleService roleService;
    
    @Autowired
    private UserRoleService userRoleService;
    
    @Override
    @EventListener(ApplicationReadyEvent.class)
    @Transactional(rollbackFor = Exception.class)
    public void initializeAdmin() {
        try {
            logger.info("开始检查并初始化默认管理员账号...");
            
            // 检查admin用户是否已存在
            User existingAdmin = userService.findByUsername(ADMIN_USERNAME);
            if (existingAdmin != null) {
                logger.info("管理员账号[{}]已存在，跳过初始化", ADMIN_USERNAME);
                return;
            }
            
            // 查找超级管理员角色
            Role adminRole = roleService.getByRoleCode(ADMIN_ROLE_CODE);
            if (adminRole == null) {
                logger.error("未找到角色代码为[{}]的角色，请先初始化角色数据", ADMIN_ROLE_CODE);
                return;
            }
            
            // 创建admin用户
            User adminUser = new User();
            adminUser.setUsername(ADMIN_USERNAME);
            adminUser.setPassword(BCrypt.hashpw(ADMIN_PASSWORD, BCrypt.gensalt()));
            adminUser.setEmail(ADMIN_EMAIL);
            adminUser.setNickname(ADMIN_NICKNAME);
            adminUser.setRole("ADMIN");
            adminUser.setCreateTime(LocalDateTime.now());
            adminUser.setUpdateTime(LocalDateTime.now());
            adminUser.setDeleted(0);
            
            boolean saveResult = userService.save(adminUser);
            if (!saveResult) {
                logger.error("保存管理员用户失败");
                return;
            }
            
            // 重新查询刚保存的用户以获取数据库分配的ID
            User savedAdmin = userService.findByUsername(ADMIN_USERNAME);
            if (savedAdmin == null) {
                logger.error("无法获取刚保存的管理员用户信息");
                return;
            }
            
            // 为管理员分配超级管理员角色
            UserRole userRole = new UserRole();
            userRole.setUserId(savedAdmin.getId());
            userRole.setRoleId(adminRole.getId());
            userRole.setCreateTime(LocalDateTime.now());
            userRole.setUpdateTime(LocalDateTime.now());
            userRole.setDeleted(0);
            
            boolean assignResult = userRoleService.save(userRole);
            if (!assignResult) {
                logger.error("为管理员分配角色失败");
                return;
            }
            
            logger.info("成功创建默认管理员账号 - 用户名: {}, 密码: {}, 角色: {}", 
                       ADMIN_USERNAME, ADMIN_PASSWORD, ADMIN_ROLE_CODE);
            
        } catch (Exception e) {
            logger.error("初始化管理员账号时发生错误", e);
            throw e;
        }
    }
}
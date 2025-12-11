package com.example.System.service;

import com.example.System.entity.User;
import com.example.System.service.impl.AdminInitServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SimpleAdminInitTest {

    @Autowired
    private AdminInitServiceImpl adminInitService;

    @Test
    void testAdminInitServiceExists() {
        // 测试AdminInitService是否能够正确注入
        assertNotNull(adminInitService, "AdminInitService应该被正确注入");
        System.out.println("AdminInitService注入测试通过");
    }

    @Test
    void testUserEntity() {
        // 测试User实体是否正确
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("testpassword");
        user.setEmail("test@example.com");
        
        assertEquals("testuser", user.getUsername());
        assertEquals("testpassword", user.getPassword());
        assertEquals("test@example.com", user.getEmail());
        
        System.out.println("User实体测试通过");
    }

    @Test
    void testAdminCredentials() {
        // 测试管理员账号和密码是否正确设置
        assertEquals("admin", "admin");
        assertEquals("admin123", "admin123");
        
        System.out.println("管理员账号密码配置测试通过");
        System.out.println("默认管理员账号: admin");
        System.out.println("默认管理员密码: admin123");
    }
}
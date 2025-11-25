package com.example.System.interceptor;

import com.example.System.annotation.RequirePermission;
import com.example.System.entity.Permission;
import com.example.System.entity.Role;
import com.example.System.service.PermissionService;
import com.example.System.service.RoleService;
import com.example.System.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        
        // 检查方法或类上是否有RequirePermission注解
        RequirePermission requirePermission = method.getAnnotation(RequirePermission.class);
        if (requirePermission == null) {
            requirePermission = method.getDeclaringClass().getAnnotation(RequirePermission.class);
        }
        
        // 如果没有权限注解，直接通过
        if (requirePermission == null) {
            return true;
        }
        
        // 从请求头中获取token
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"code\": 401, \"message\": \"未提供有效的token\"}");
            return false;
        }
        
        token = token.substring(7); // 去掉"Bearer "前缀
        
        // 验证token
        try {
            String username = jwtUtil.getUsernameFromToken(token);
            if (username == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"code\": 401, \"message\": \"token无效\"}");
                return false;
            }
            
            Long userId = jwtUtil.getUserIdFromToken(token);
            
            // 检查角色权限
            String[] requiredRoles = requirePermission.roles();
            if (requiredRoles.length > 0) {
                List<Role> userRoles = roleService.getRolesByUserId(userId);
                List<String> userRoleCodes = userRoles.stream()
                        .map(Role::getRoleCode)
                        .collect(Collectors.toList());
                
                boolean hasRole = Arrays.stream(requiredRoles)
                        .anyMatch(userRoleCodes::contains);
                
                if (!hasRole) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write("{\"code\": 403, \"message\": \"权限不足：缺少所需角色\"}");
                    return false;
                }
            }
            
            // 检查权限
            String[] requiredPermissions = requirePermission.value();
            if (requiredPermissions.length > 0) {
                List<Permission> userPermissions = permissionService.getPermissionsByUserId(userId);
                List<String> userPermissionCodes = userPermissions.stream()
                        .map(Permission::getPermissionCode)
                        .collect(Collectors.toList());
                
                boolean hasPermission;
                if (requirePermission.logical() == RequirePermission.Logical.ALL) {
                    // 需要满足所有权限
                    hasPermission = Arrays.stream(requiredPermissions)
                            .allMatch(userPermissionCodes::contains);
                } else {
                    // 满足任一权限即可
                    hasPermission = Arrays.stream(requiredPermissions)
                            .anyMatch(userPermissionCodes::contains);
                }
                
                if (!hasPermission) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write("{\"code\": 403, \"message\": \"权限不足：缺少所需权限\"}");
                    return false;
                }
            }
            
            // 将用户信息放入请求属性中，供后续使用
            request.setAttribute("userId", userId);
            request.setAttribute("username", username);
            
            return true;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"code\": 401, \"message\": \"token验证失败\"}");
            return false;
        }
    }
}
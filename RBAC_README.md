# RBAC权限管理系统

本系统已升级为基于角色的访问控制(RBAC)系统，实现了细粒度的权限管理。

## 系统架构

### 核心概念

1. **用户(User)**: 系统的使用者
2. **角色(Role)**: 权限的集合
3. **权限(Permission)**: 对资源的访问控制
4. **用户角色关系(UserRole)**: 用户和角色的多对多关系
5. **角色权限关系(RolePermission)**: 角色和权限的多对多关系

### 实体类

- `User`: 用户实体
- `Role`: 角色实体
- `Permission`: 权限实体
- `UserRole`: 用户角色关系实体
- `RolePermission`: 角色权限关系实体

## 功能特性

1. **JWT令牌认证**: 基于JWT的无状态认证
2. **基于注解的权限控制**: 使用`@RequirePermission`注解进行方法级权限控制
3. **角色和权限管理**: 提供完整的角色和权限管理接口
4. **用户角色分配**: 支持为用户分配和取消角色

## 使用指南

### 1. 数据库初始化

首先执行`src/main/resources/sql/rbac_init.sql`脚本初始化RBAC相关表结构：

```sql
-- 执行sql/rbac_init.sql文件
```

### 2. 使用权限注解

在Controller方法上使用`@RequirePermission`注解进行权限控制：

```java
// 检查角色
@RequirePermission(roles = {"admin"})
public Result<String> adminEndpoint() {
    // 只有admin角色可以访问
}

// 检查权限
@RequirePermission(value = {"user:view"})
public Result<String> userViewEndpoint() {
    // 只有拥有user:view权限的用户可以访问
}

// 检查多个权限(满足任一个即可)
@RequirePermission(value = {"user:view", "role:view"}, logical = RequirePermission.Logical.ANY)
public Result<String> anyPermissionEndpoint() {
    // 拥有user:view或role:view权限即可访问
}

// 检查多个权限(必须满足所有)
@RequirePermission(value = {"user:view", "role:view"}, logical = RequirePermission.Logical.ALL)
public Result<String> allPermissionsEndpoint() {
    // 必须同时拥有user:view和role:view权限才能访问
}

// 同时检查角色和权限
@RequirePermission(roles = {"admin"}, value = {"user:manage"})
public Result<String> complexEndpoint() {
    // 必须是admin角色且拥有user:manage权限才能访问
}
```

### 3. API请求

登录后，在请求头中携带JWT令牌：

```
Authorization: Bearer <token>
```

### 4. API接口

#### 认证接口
- `POST /auth/login`: 用户登录
- `POST /auth/register`: 用户注册
- `GET /auth/info`: 获取用户信息

#### 角色管理接口
- `GET /role/list`: 获取所有角色
- `POST /role/create`: 创建角色
- `PUT /role/update/{id}`: 更新角色
- `DELETE /role/delete/{id}`: 删除角色

#### 权限管理接口
- `GET /permission/list`: 获取所有权限
- `POST /permission/create`: 创建权限
- `PUT /permission/update/{id}`: 更新权限
- `DELETE /permission/delete/{id}`: 删除权限

#### 用户角色管理接口
- `GET /user-role/user/{userId}/roles`: 获取用户的角色
- `GET /user-role/user/{userId}/permissions`: 获取用户的权限
- `POST /user-role/assign`: 为用户分配角色
- `DELETE /user-role/remove`: 取消用户角色

#### 用户管理接口
- `GET /user/list`: 获取用户列表
- `GET /user/detail/{id}`: 获取用户详情
- `POST /user/create`: 创建用户
- `PUT /user/update/{id}`: 更新用户
- `DELETE /user/delete/{id}`: 删除用户

#### 测试接口
- `GET /test/public`: 公开接口，无需权限
- `GET /test/user`: 需要用户角色
- `GET /test/admin`: 需要管理员角色
- `GET /test/user-view`: 需要用户查看权限
- `GET /test/user-manage`: 需要用户管理权限
- `GET /test/any-permission`: 需要多个权限中的任一个
- `GET /test/all-permissions`: 需要所有权限

## 默认角色和权限

系统预设了三种角色：

1. **admin(超级管理员)**: 拥有所有权限
2. **manager(普通管理员)**: 拥有部分管理权限
3. **user(普通用户)**: 只有基本操作权限

## 示例流程

1. 使用admin账号登录获取token
2. 使用token创建角色和权限
3. 为用户分配角色
4. 为角色分配权限
5. 用户使用自己的账号登录，系统会返回其权限信息
6. 在后续请求中携带token，系统会自动验证权限

## 注意事项

1. 登录接口和注册接口不需要权限验证
2. 所有需要权限验证的接口都需要在请求头中携带有效的JWT令牌
3. 权限验证基于用户拥有的所有角色和这些角色关联的所有权限
4. 令牌验证失败或权限不足会返回相应的错误信息
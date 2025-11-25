# 数据库表初始化指南

本系统提供了多种方式来自动初始化RBAC相关的数据库表。

## 方式一：使用应用启动自动初始化（推荐）

### 1. 配置启用

在 `application.yml` 中确认已启用自动初始化：

```yaml
# 应用自定义配置
app:
  database:
    # 是否自动初始化数据库表（开发环境建议设为true，生产环境建议设为false）
    auto-init: true
```

### 2. 启动应用

直接启动Spring Boot应用，系统会自动检查表是否存在：

- 使用 `SHOW TABLES` 命令检查表是否存在，更准确可靠
- 如果表不存在，会逐个创建
- 初始化完成后会插入默认的角色和权限数据

### 3. 日志输出

应用启动时，你会看到类似以下的日志：

```
INFO  --- o.e.S.config.SafeTableInitializer     : 开始安全初始化数据库表...
INFO  --- o.e.S.config.SafeTableInitializer     : 角色表已存在，跳过创建
INFO  --- o.e.S.config.SafeTableInitializer     : 权限表已存在，跳过创建
INFO  --- o.e.S.config.SafeTableInitializer     : 数据库表安全初始化完成！
```

## 为什么选择 SafeTableInitializer

在最初的实现中，我们遇到了以下问题：

1. **表检查不准确**：使用简单的查询方式检查表是否存在不够准确
2. **SQL执行问题**：多个SQL语句一起执行时可能存在语法错误
3. **多个初始化器冲突**：同时运行多个初始化器导致冲突

`SafeTableInitializer` 解决了这些问题：

1. **使用 `SHOW TABLES`**：更准确地检查表是否存在
2. **逐个执行SQL**：避免批量执行SQL时的语法问题
3. **单一初始化器**：只使用一个初始化器，避免冲突
4. **错误处理**：更好的错误处理和日志记录

## 方式二：手动执行SQL脚本

### 1. 执行初始化脚本

直接执行 `src/main/resources/sql/check_and_init_tables.sql` 脚本：

```bash
mysql -u root -p systemDome < src/main/resources/sql/check_and_init_tables.sql
```

或者通过MySQL客户端工具执行该脚本文件。

### 2. 脚本特点

- 使用 `CREATE TABLE IF NOT EXISTS` 语句，避免重复创建
- 使用 `INSERT IGNORE INTO` 语句，避免重复插入数据
- 包含所有RBAC相关表的创建和默认数据的插入

## 初始化内容

### 表结构

1. **role** - 角色表
2. **permission** - 权限表
3. **role_permission** - 角色权限关系表
4. **user_role** - 用户角色关系表

### 默认角色

1. **admin** - 超级管理员，拥有所有权限
2. **manager** - 普通管理员，拥有部分管理权限
3. **user** - 普通用户，只有基本操作权限

### 默认权限

- 用户管理相关权限（查看、创建、更新、删除、管理）
- 角色管理相关权限（查看、创建、更新、删除、管理）
- 权限管理相关权限（查看、创建、更新、删除、管理）
- 用户角色管理权限

### 默认关系

- admin角色分配所有权限
- manager角色分配部分权限（查看权限和更新权限）
- user角色分配基本权限（用户查看权限）

## 生产环境建议

在生产环境中，建议：

1. 将 `app.database.auto-init` 设置为 `false`
2. 手动执行SQL脚本初始化数据库
3. 由DBA审核和执行数据库变更

## 方式三：开发环境快速重置（危险操作）

⚠️ **警告：此操作将删除所有RBAC相关表和数据！仅限开发环境使用！**

在开发环境中，如果你想完全重置RBAC相关表，可以：

1. 在 `application.yml` 中添加以下配置：

```yaml
# 应用自定义配置
app:
  database:
    # 启用数据库重置功能（危险！仅限开发环境）
    reset: true
```

2. 启动应用，然后调用 `ResetDatabase` 类的 `resetAllTables()` 方法

3. 重置完成后，请务必移除 `app.database.reset: true` 配置

## 注意事项

1. 确保数据库 `systemDome` 已存在
2. 确保数据库用户有足够的权限创建表和插入数据
3. 如果表已存在，不会重复创建
4. 如果数据已存在，不会重复插入（使用 `INSERT IGNORE`）
5. 如果已存在admin用户，会自动为其分配admin角色
6. 生产环境中**切勿**启用 `app.database.reset` 配置项
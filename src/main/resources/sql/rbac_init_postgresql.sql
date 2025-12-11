-- PostgreSQL版本的RBAC相关表结构和数据初始化脚本

-- 角色表
CREATE TABLE IF NOT EXISTS "role" (
  "id" BIGSERIAL PRIMARY KEY,
  "role_name" VARCHAR(50) NOT NULL,
  "role_code" VARCHAR(50) NOT NULL,
  "description" VARCHAR(255),
  "create_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  "update_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  "deleted" SMALLINT DEFAULT 0
);

-- 创建唯一索引
CREATE UNIQUE INDEX IF NOT EXISTS "uk_role_code" ON "role" ("role_code");

-- 权限表
CREATE TABLE IF NOT EXISTS "permission" (
  "id" BIGSERIAL PRIMARY KEY,
  "permission_name" VARCHAR(50) NOT NULL,
  "permission_code" VARCHAR(100) NOT NULL,
  "resource_type" VARCHAR(50),
  "resource_url" VARCHAR(255),
  "parent_id" BIGINT DEFAULT 0,
  "description" VARCHAR(255),
  "create_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  "update_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  "deleted" SMALLINT DEFAULT 0
);

-- 创建唯一索引
CREATE UNIQUE INDEX IF NOT EXISTS "uk_permission_code" ON "permission" ("permission_code");

-- 角色权限关系表
CREATE TABLE IF NOT EXISTS "role_permission" (
  "id" BIGSERIAL PRIMARY KEY,
  "role_id" BIGINT NOT NULL,
  "permission_id" BIGINT NOT NULL,
  "create_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  "update_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  "deleted" SMALLINT DEFAULT 0
);

-- 创建唯一索引
CREATE UNIQUE INDEX IF NOT EXISTS "uk_role_permission" ON "role_permission" ("role_id", "permission_id");

-- 用户角色关系表
CREATE TABLE IF NOT EXISTS "user_role" (
  "id" BIGSERIAL PRIMARY KEY,
  "user_id" BIGINT NOT NULL,
  "role_id" BIGINT NOT NULL,
  "create_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  "update_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  "deleted" SMALLINT DEFAULT 0
);

-- 创建唯一索引
CREATE UNIQUE INDEX IF NOT EXISTS "uk_user_role" ON "user_role" ("user_id", "role_id");

-- 插入默认角色（使用ON CONFLICT避免重复插入）
INSERT INTO "role" ("role_name", "role_code", "description") VALUES 
('超级管理员', 'admin', '系统超级管理员，拥有所有权限'),
('普通管理员', 'manager', '普通管理员，拥有部分管理权限'),
('普通用户', 'user', '普通用户，只有基本操作权限')
ON CONFLICT ("role_code") DO NOTHING;

-- 插入默认权限
INSERT INTO "permission" ("permission_name", "permission_code", "resource_type", "resource_url", "description") VALUES 
('用户查看', 'user:view', 'menu', '/user/list', '查看用户列表'),
('用户创建', 'user:create', 'button', '/user/create', '创建用户'),
('用户更新', 'user:update', 'button', '/user/update', '更新用户'),
('用户删除', 'user:delete', 'button', '/user/delete', '删除用户'),
('角色查看', 'role:view', 'menu', '/role/list', '查看角色列表'),
('角色管理', 'role:manage', 'menu', '/role/**', '管理角色'),
('权限查看', 'permission:view', 'menu', '/permission/list', '查看权限列表'),
('权限管理', 'permission:manage', 'menu', '/permission/**', '管理权限'),
('用户角色管理', 'user:role:manage', 'menu', '/user-role/**', '管理用户角色关系')
ON CONFLICT ("permission_code") DO NOTHING;

-- 为admin角色分配所有权限
INSERT INTO "role_permission" ("role_id", "permission_id") 
SELECT 1, "id" FROM "permission"
WHERE NOT EXISTS (
    SELECT 1 FROM "role_permission" WHERE "role_id" = 1 AND "permission_id" = "permission"."id"
);

-- 为manager角色分配部分权限
INSERT INTO "role_permission" ("role_id", "permission_id") 
SELECT 2, "id" FROM "permission" 
WHERE "permission_code" IN ('user:view', 'user:update', 'role:view', 'permission:view')
AND NOT EXISTS (
    SELECT 1 FROM "role_permission" WHERE "role_id" = 2 AND "permission_id" = "permission"."id"
);

-- 为user角色分配基本权限
INSERT INTO "role_permission" ("role_id", "permission_id") 
SELECT 3, "id" FROM "permission" 
WHERE "permission_code" IN ('user:view')
AND NOT EXISTS (
    SELECT 1 FROM "role_permission" WHERE "role_id" = 3 AND "permission_id" = "permission"."id"
);

-- 如果已存在admin用户，则为其分配admin角色
INSERT INTO "user_role" ("user_id", "role_id") 
SELECT "id", 1 FROM "user" WHERE "username" = 'admin'
ON CONFLICT ("user_id", "role_id") DO NOTHING;
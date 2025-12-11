-- PostgreSQL版本的RBAC表初始化脚本

-- 创建更新时间触发器函数
CREATE OR REPLACE FUNCTION update_modified_column() RETURNS TRIGGER AS $$
BEGIN
    NEW.update_time = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- 创建用户表（如果不存在）
CREATE TABLE IF NOT EXISTS "user" (
    "id" BIGSERIAL PRIMARY KEY,
    "username" VARCHAR(50) NOT NULL,
    "password" VARCHAR(255) NOT NULL,
    "email" VARCHAR(100) DEFAULT NULL,
    "phone" VARCHAR(20) DEFAULT NULL,
    "nickname" VARCHAR(50) DEFAULT NULL,
    "avatar" VARCHAR(255) DEFAULT NULL,
    "role" VARCHAR(20) DEFAULT 'USER',
    "create_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    "update_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    "deleted" INTEGER DEFAULT 0,
    CONSTRAINT uk_username UNIQUE ("username")
);

-- 为user表创建自动更新触发器
DROP TRIGGER IF EXISTS update_user_modtime ON "user";
CREATE TRIGGER update_user_modtime BEFORE UPDATE ON "user" FOR EACH ROW EXECUTE FUNCTION update_modified_column();

-- 创建角色表（如果不存在）
CREATE TABLE IF NOT EXISTS role (
    id BIGSERIAL PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL,
    role_code VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted INTEGER DEFAULT 0
);

-- 创建权限表（如果不存在）
CREATE TABLE IF NOT EXISTS permission (
    id BIGSERIAL PRIMARY KEY,
    permission_name VARCHAR(50) NOT NULL,
    permission_code VARCHAR(100) NOT NULL UNIQUE,
    resource_type VARCHAR(50),
    resource_url VARCHAR(255),
    parent_id BIGINT DEFAULT 0,
    description VARCHAR(255),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted INTEGER DEFAULT 0
);

-- 创建角色权限关系表（如果不存在）
CREATE TABLE IF NOT EXISTS role_permission (
    id BIGSERIAL PRIMARY KEY,
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted INTEGER DEFAULT 0,
    UNIQUE(role_id, permission_id)
);

-- 创建用户角色关系表（如果不存在）
CREATE TABLE IF NOT EXISTS user_role (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted INTEGER DEFAULT 0,
    UNIQUE(user_id, role_id)
);

-- 为需要自动更新update_time的表创建触发器
DROP TRIGGER IF EXISTS update_role_modtime ON role;
CREATE TRIGGER update_role_modtime BEFORE UPDATE ON role FOR EACH ROW EXECUTE FUNCTION update_modified_column();

DROP TRIGGER IF EXISTS update_permission_modtime ON permission;
CREATE TRIGGER update_permission_modtime BEFORE UPDATE ON permission FOR EACH ROW EXECUTE FUNCTION update_modified_column();

DROP TRIGGER IF EXISTS update_role_permission_modtime ON role_permission;
CREATE TRIGGER update_role_permission_modtime BEFORE UPDATE ON role_permission FOR EACH ROW EXECUTE FUNCTION update_modified_column();

DROP TRIGGER IF EXISTS update_user_role_modtime ON user_role;
CREATE TRIGGER update_user_role_modtime BEFORE UPDATE ON user_role FOR EACH ROW EXECUTE FUNCTION update_modified_column();

-- 插入默认角色（如果不存在）
INSERT INTO role (role_name, role_code, description) VALUES 
('超级管理员', 'admin', '系统超级管理员，拥有所有权限'),
('普通管理员', 'manager', '普通管理员，拥有部分管理权限'),
('普通用户', 'user', '普通用户，只有基本操作权限')
ON CONFLICT (role_code) DO NOTHING;

-- 插入默认权限（如果不存在）
INSERT INTO permission (permission_name, permission_code, resource_type, resource_url, description) VALUES 
('用户查看', 'user:view', 'menu', '/user/list', '查看用户列表'),
('用户创建', 'user:create', 'button', '/user/create', '创建用户'),
('用户更新', 'user:update', 'button', '/user/update', '更新用户'),
('用户删除', 'user:delete', 'button', '/user/delete', '删除用户'),
('用户管理', 'user:manage', 'menu', '/user/**', '管理用户'),
('角色查看', 'role:view', 'menu', '/role/list', '查看角色列表'),
('角色创建', 'role:create', 'button', '/role/create', '创建角色'),
('角色更新', 'role:update', 'button', '/role/update', '更新角色'),
('角色删除', 'role:delete', 'button', '/role/delete', '删除角色'),
('角色管理', 'role:manage', 'menu', '/role/**', '管理角色'),
('权限查看', 'permission:view', 'menu', '/permission/list', '查看权限列表'),
('权限创建', 'permission:create', 'button', '/permission/create', '创建权限'),
('权限更新', 'permission:update', 'button', '/permission/update', '更新权限'),
('权限删除', 'permission:delete', 'button', '/permission/delete', '删除权限'),
('权限管理', 'permission:manage', 'menu', '/permission/**', '管理权限'),
('用户角色管理', 'user:role:manage', 'menu', '/user-role/**', '管理用户角色关系')
ON CONFLICT (permission_code) DO NOTHING;

-- 为admin角色分配所有权限（如果不存在）
INSERT INTO role_permission (role_id, permission_id) 
SELECT 1, id FROM permission
ON CONFLICT (role_id, permission_id) DO NOTHING;

-- 为manager角色分配部分权限（如果不存在）
INSERT INTO role_permission (role_id, permission_id) 
SELECT 2, id FROM permission 
WHERE permission_code IN ('user:view', 'user:update', 'role:view', 'permission:view')
ON CONFLICT (role_id, permission_id) DO NOTHING;

-- 为user角色分配基本权限（如果不存在）
INSERT INTO role_permission (role_id, permission_id) 
SELECT 3, id FROM permission 
WHERE permission_code IN ('user:view')
ON CONFLICT (role_id, permission_id) DO NOTHING;

-- 如果已存在admin用户，则为其分配admin角色（如果不存在）
INSERT INTO user_role (user_id, role_id) 
SELECT id, 1 FROM "user" WHERE username = 'admin'
ON CONFLICT (user_id, role_id) DO NOTHING;
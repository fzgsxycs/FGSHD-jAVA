-- RBAC相关表结构

-- 角色表
CREATE TABLE IF NOT EXISTS `role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_name` varchar(50) NOT NULL COMMENT '角色名称',
  `role_code` varchar(50) NOT NULL COMMENT '角色代码',
  `description` varchar(255) DEFAULT NULL COMMENT '角色描述',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 权限表
CREATE TABLE IF NOT EXISTS `permission` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `permission_name` varchar(50) NOT NULL COMMENT '权限名称',
  `permission_code` varchar(100) NOT NULL COMMENT '权限代码',
  `resource_type` varchar(50) DEFAULT NULL COMMENT '资源类型',
  `resource_url` varchar(255) DEFAULT NULL COMMENT '资源URL',
  `parent_id` bigint DEFAULT '0' COMMENT '父级ID',
  `description` varchar(255) DEFAULT NULL COMMENT '权限描述',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_permission_code` (`permission_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';

-- 角色权限关系表
CREATE TABLE IF NOT EXISTS `role_permission` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `permission_id` bigint NOT NULL COMMENT '权限ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_permission` (`role_id`,`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关系表';

-- 用户角色关系表
CREATE TABLE IF NOT EXISTS `user_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_role` (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关系表';

-- 插入默认角色
INSERT INTO `role` (`role_name`, `role_code`, `description`) VALUES 
('超级管理员', 'admin', '系统超级管理员，拥有所有权限'),
('普通管理员', 'manager', '普通管理员，拥有部分管理权限'),
('普通用户', 'user', '普通用户，只有基本操作权限');

-- 插入默认权限
INSERT INTO `permission` (`permission_name`, `permission_code`, `resource_type`, `resource_url`, `description`) VALUES 
('用户查看', 'user:view', 'menu', '/user/list', '查看用户列表'),
('用户创建', 'user:create', 'button', '/user/create', '创建用户'),
('用户更新', 'user:update', 'button', '/user/update', '更新用户'),
('用户删除', 'user:delete', 'button', '/user/delete', '删除用户'),
('角色查看', 'role:view', 'menu', '/role/list', '查看角色列表'),
('角色管理', 'role:manage', 'menu', '/role/**', '管理角色'),
('权限查看', 'permission:view', 'menu', '/permission/list', '查看权限列表'),
('权限管理', 'permission:manage', 'menu', '/permission/**', '管理权限'),
('用户角色管理', 'user:role:manage', 'menu', '/user-role/**', '管理用户角色关系');

-- 为admin角色分配所有权限
INSERT INTO `role_permission` (`role_id`, `permission_id`) 
SELECT 1, id FROM `permission`;

-- 为manager角色分配部分权限
INSERT INTO `role_permission` (`role_id`, `permission_id`) 
SELECT 2, id FROM `permission` 
WHERE `permission_code` IN ('user:view', 'user:update', 'role:view', 'permission:view');

-- 为user角色分配基本权限
INSERT INTO `role_permission` (`role_id`, `permission_id`) 
SELECT 3, id FROM `permission` 
WHERE `permission_code` IN ('user:view');

-- 如果已存在admin用户，则为其分配admin角色
INSERT IGNORE INTO `user_role` (`user_id`, `role_id`) 
SELECT id, 1 FROM `user` WHERE `username` = 'admin';
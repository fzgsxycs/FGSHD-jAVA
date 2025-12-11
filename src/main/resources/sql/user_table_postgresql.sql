-- 创建用户表（PostgreSQL版本）
CREATE TABLE IF NOT EXISTS "user" (
    "id" BIGSERIAL PRIMARY KEY COMMENT '主键ID',
    "username" VARCHAR(50) NOT NULL COMMENT '用户名',
    "password" VARCHAR(255) NOT NULL COMMENT '密码',
    "email" VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    "phone" VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    "nickname" VARCHAR(50) DEFAULT NULL COMMENT '昵称',
    "avatar" VARCHAR(255) DEFAULT NULL COMMENT '头像',
    "role" VARCHAR(20) DEFAULT 'USER' COMMENT '角色',
    "create_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    "update_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    "deleted" INTEGER DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    CONSTRAINT "uk_username" UNIQUE ("username")
);

-- 插入测试数据（密码为123456）
INSERT INTO "user" ("username", "password", "email", "nickname", "role") 
VALUES ('admin', '$2a$10$7JB720yubVSOfvamAb6u/.6Od1d2.6.Ea3BqR3sKjOOv2XKX3BT5G', 'admin@example.com', '管理员', 'ADMIN');
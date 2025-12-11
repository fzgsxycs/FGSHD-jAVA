-- 创建异常日志表
CREATE TABLE IF NOT EXISTS sys_exception_log (
    id BIGSERIAL PRIMARY KEY,
    trace_id VARCHAR(64) COMMENT '追踪ID',
    exception_type VARCHAR(100) NOT NULL COMMENT '异常类型',
    error_code INT COMMENT '错误码',
    error_message TEXT COMMENT '错误消息',
    request_uri VARCHAR(500) COMMENT '请求URI',
    request_method VARCHAR(10) COMMENT '请求方法',
    request_params TEXT COMMENT '请求参数',
    user_id BIGINT COMMENT '用户ID',
    username VARCHAR(100) COMMENT '用户名',
    ip_address VARCHAR(50) COMMENT 'IP地址',
    user_agent TEXT COMMENT 'User-Agent',
    stack_trace TEXT COMMENT '异常堆栈',
    handled BOOLEAN DEFAULT FALSE COMMENT '是否已处理',
    handler VARCHAR(100) COMMENT '处理人',
    handle_note TEXT COMMENT '处理备注',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间'
);

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_exception_log_trace_id ON sys_exception_log(trace_id);
CREATE INDEX IF NOT EXISTS idx_exception_log_exception_type ON sys_exception_log(exception_type);
CREATE INDEX IF NOT EXISTS idx_exception_log_error_code ON sys_exception_log(error_code);
CREATE INDEX IF NOT EXISTS idx_exception_log_create_time ON sys_exception_log(create_time);
CREATE INDEX IF NOT EXISTS idx_exception_log_handled ON sys_exception_log(handled);
CREATE INDEX IF NOT EXISTS idx_exception_log_user_id ON sys_exception_log(user_id);

-- 添加表注释
COMMENT ON TABLE sys_exception_log IS '异常日志表';

-- 添加字段注释
COMMENT ON COLUMN sys_exception_log.id IS '主键ID';
COMMENT ON COLUMN sys_exception_log.trace_id IS '追踪ID';
COMMENT ON COLUMN sys_exception_log.exception_type IS '异常类型';
COMMENT ON COLUMN sys_exception_log.error_code IS '错误码';
COMMENT ON COLUMN sys_exception_log.error_message IS '错误消息';
COMMENT ON COLUMN sys_exception_log.request_uri IS '请求URI';
COMMENT ON COLUMN sys_exception_log.request_method IS '请求方法';
COMMENT ON COLUMN sys_exception_log.request_params IS '请求参数';
COMMENT ON COLUMN sys_exception_log.user_id IS '用户ID';
COMMENT ON COLUMN sys_exception_log.username IS '用户名';
COMMENT ON COLUMN sys_exception_log.ip_address IS 'IP地址';
COMMENT ON COLUMN sys_exception_log.user_agent IS 'User-Agent';
COMMENT ON COLUMN sys_exception_log.stack_trace IS '异常堆栈';
COMMENT ON COLUMN sys_exception_log.handled IS '是否已处理';
COMMENT ON COLUMN sys_exception_log.handler IS '处理人';
COMMENT ON COLUMN sys_exception_log.handle_note IS '处理备注';
COMMENT ON COLUMN sys_exception_log.create_time IS '创建时间';
COMMENT ON COLUMN sys_exception_log.update_time IS '更新时间';
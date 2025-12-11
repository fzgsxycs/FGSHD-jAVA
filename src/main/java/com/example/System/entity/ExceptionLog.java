package com.example.System.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 异常日志实体
 * 
 * @author System
 */
@Data
@Accessors(chain = true)
@TableName("sys_exception_log")
public class ExceptionLog {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 追踪ID
     */
    @TableField("trace_id")
    private String traceId;

    /**
     * 异常类型
     */
    @TableField("exception_type")
    private String exceptionType;

    /**
     * 错误码
     */
    @TableField("error_code")
    private Integer errorCode;

    /**
     * 错误消息
     */
    @TableField("error_message")
    private String errorMessage;

    /**
     * 请求URI
     */
    @TableField("request_uri")
    private String requestUri;

    /**
     * 请求方法
     */
    @TableField("request_method")
    private String requestMethod;

    /**
     * 请求参数
     */
    @TableField("request_params")
    private String requestParams;

    /**
     * 用户ID（如果已登录）
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 用户名（如果已登录）
     */
    @TableField("username")
    private String username;

    /**
     * IP地址
     */
    @TableField("ip_address")
    private String ipAddress;

    /**
     * User-Agent
     */
    @TableField("user_agent")
    private String userAgent;

    /**
     * 异常堆栈
     */
    @TableField("stack_trace")
    private String stackTrace;

    /**
     * 是否已处理
     */
    @TableField("handled")
    private Boolean handled;

    /**
     * 处理人
     */
    @TableField("handler")
    private String handler;

    /**
     * 处理备注
     */
    @TableField("handle_note")
    private String handleNote;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    // 手动添加getter和setter方法以确保编译成功
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTraceId() {
        return traceId;
    }
    
    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }
    
    public String getExceptionType() {
        return exceptionType;
    }
    
    public void setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
    }
    
    public Integer getErrorCode() {
        return errorCode;
    }
    
    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public String getRequestUri() {
        return requestUri;
    }
    
    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }
    
    public String getRequestMethod() {
        return requestMethod;
    }
    
    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }
    
    public String getRequestParams() {
        return requestParams;
    }
    
    public void setRequestParams(String requestParams) {
        this.requestParams = requestParams;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getIpAddress() {
        return ipAddress;
    }
    
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    
    public String getUserAgent() {
        return userAgent;
    }
    
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
    
    public String getStackTrace() {
        return stackTrace;
    }
    
    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }
    
    public Boolean getHandled() {
        return handled;
    }
    
    public void setHandled(Boolean handled) {
        this.handled = handled;
    }
    
    public String getHandler() {
        return handler;
    }
    
    public void setHandler(String handler) {
        this.handler = handler;
    }
    
    public String getHandleNote() {
        return handleNote;
    }
    
    public void setHandleNote(String handleNote) {
        this.handleNote = handleNote;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
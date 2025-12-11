# 异常处理机制测试指南

本文档提供了测试Spring Boot应用程序异常处理机制的详细指南。

## 准备工作

1. 确保已编译项目：
   ```bash
   mvn clean compile
   ```

2. 启动应用程序：
   ```bash
   mvn spring-boot:run
   ```

3. 确保应用程序正在运行在 http://localhost:8080

## 测试工具

项目提供了三种测试异常处理机制的工具：

### 1. HTML测试页面

打开 `exception-test.html` 文件在浏览器中：
- 双击文件在浏览器中打开
- 或者在浏览器中输入文件路径
- 提供了图形化界面，点击按钮即可测试各种异常

### 2. Python测试脚本

运行 `test-exceptions.py` 脚本：
```bash
python test-exceptions.py
```

要求：
- Python 3.x
- 安装requests库：`pip install requests`

### 3. Shell测试脚本

运行 `test-exceptions.sh` 脚本：
```bash
bash test-exceptions.sh
```

要求：
- Bash shell
- curl命令
- 可选：jq命令（用于格式化JSON输出）

## 测试用例

测试用例包括以下异常类型：

### 自定义异常

1. **业务异常**
   - 无数据：`/exception-test/business?withData=false`
   - 带数据：`/exception-test/business?withData=true`

2. **认证异常**
   - 端点：`/exception-test/auth`

3. **系统异常**
   - 端点：`/exception-test/system`

### Java内置异常

1. **空指针异常**
   - 端点：`/exception-test/nullpointer`

2. **数组越界异常**
   - 端点：`/exception-test/array`

3. **数字格式异常**
   - 端点：`/exception-test/numberformat?number=abc`

4. **算术异常**
   - 端点：`/exception-test/arithmetic`

5. **类型转换异常**
   - 端点：`/exception-test/classcast`

6. **IllegalArgumentException**
   - 空参数：`/exception-test/illegal-argument?value=`
   - 有效参数：`/exception-test/illegal-argument?value=test`

## 异常处理机制验证

测试异常处理机制时，请验证以下几点：

### 1. HTTP状态码

- 业务异常：通常返回400 Bad Request
- 认证异常：返回401 Unauthorized或403 Forbidden
- 系统异常：返回500 Internal Server Error
- 其他异常：返回适当的HTTP状态码

### 2. 响应格式

所有错误响应应具有一致的格式：
```json
{
  "code": 错误码,
  "message": "错误消息",
  "data": null,
  "timestamp": "时间戳",
  "traceId": "追踪ID"
}
```

### 3. 追踪ID

- 每个异常响应应包含唯一的traceId
- traceId可用于在数据库中查询对应的异常日志

### 4. 异常日志记录

验证异常是否正确记录到数据库：
```sql
SELECT * FROM sys_exception_log WHERE trace_id = '你的追踪ID';
```

检查日志中是否包含：
- 异常类型
- 错误码
- 错误消息
- 请求URI
- 请求参数
- IP地址
- 用户信息（如果有）
- 异常堆栈

## 自定义测试

您可以创建自己的测试用例来测试特定场景：

### 在Java代码中抛出异常

```java
// 抛出业务异常
throw new BusinessException(ErrorCode.USER_NOT_FOUND, "用户不存在");

// 抛出认证异常
throw new AuthException(ErrorCode.UNAUTHORIZED, "未授权访问");

// 抛出系统异常
throw new SystemException(ErrorCode.DATABASE_ERROR, "数据库连接失败");
```

### 在HTML测试页面中添加新按钮

```html
<button onclick="testException('/your/custom/endpoint')">自定义测试</button>
```

### 在Python或Shell脚本中添加新测试

```python
test_endpoint("/your/custom/endpoint", "自定义测试")
```

或

```bash
test_endpoint "/your/custom/endpoint" "自定义测试"
```

## 常见问题

1. **无法连接到服务器**
   - 确保Spring Boot应用程序正在运行
   - 检查端口是否正确（默认8080）
   - 检查防火墙设置

2. **响应不是JSON格式**
   - 可能是未处理的异常，检查服务器日志
   - 确保GlobalExceptionHandler正确配置

3. **没有traceId**
   - 检查GlobalExceptionHandler是否正确实现
   - 确保ExceptionLogService正确注入

## 总结

通过这些测试工具，您可以全面验证应用程序的异常处理机制，确保：
- 所有异常都被正确捕获和处理
- 返回一致的错误响应格式
- 异常日志被正确记录
- 系统具有良好的错误处理能力

如果您发现任何问题或需要改进，请相应地调整代码。
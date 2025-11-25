# Java 登录系统

基于 Spring Boot + MyBatis Plus + JWT 的登录系统

## 技术栈
- Spring Boot 3.0.2
- MyBatis Plus 3.5.3.1
- MySQL
- JWT
- Lombok
- Hutool
- SpringDoc OpenAPI (Swagger)

## 项目结构
```
src/main/java/com/example/System/
├── config/           # 配置类
├── controller/       # 控制器
├── dto/             # 数据传输对象
├── entity/          # 实体类
├── handler/         # 元数据处理器
├── mapper/          # MyBatis Plus Mapper
├── service/         # 服务接口
├── service/impl/    # 服务实现
├── util/            # 工具类
└── SystemApplication.java  # 启动类
```

## 接口文档

### 1. 用户登录
- **URL**: `/auth/login`
- **方法**: POST
- **请求体**:
```json
{
    "username": "admin",
    "password": "123456"
}
```
- **响应**:
```json
{
    "code": 200,
    "message": "操作成功",
    "data": {
        "token": "eyJhbGciOiJIUzUxMiJ9...",
        "userId": 1,
        "username": "admin",
        "nickname": "管理员",
        "role": "ADMIN",
        "avatar": null
    }
}
```

### 2. 用户注册
- **URL**: `/auth/register`
- **方法**: POST
- **请求体**:
```json
{
    "username": "testuser",
    "password": "123456",
    "email": "test@example.com",
    "nickname": "测试用户",
    "phone": "13800138000"
}
```
- **响应**:
```json
{
    "code": 200,
    "message": "注册成功",
    "data": "注册成功"
}
```

### 3. 获取用户信息
- **URL**: `/auth/info?userId=1`
- **方法**: GET
- **响应**:
```json
{
    "code": 200,
    "message": "操作成功",
    "data": {
        "id": 1,
        "username": "admin",
        "password": null,
        "email": "admin@example.com",
        "phone": null,
        "nickname": "管理员",
        "avatar": null,
        "role": "ADMIN",
        "createTime": "2023-01-01T12:00:00",
        "updateTime": "2023-01-01T12:00:00",
        "deleted": 0
    }
}
```

## 配置说明

### 1. 数据库配置
修改 `src/main/resources/application.yml` 中的数据库连接信息：
```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/system_db?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B8
    username: root
    password: 123456
```

### 2. JWT配置
```yaml
jwt:
  secret: mySecretKey  # JWT密钥
  expiration: 86400000  # 过期时间（24小时）
```

## 启动步骤

1. 创建数据库 `system_db`
2. 执行 `src/main/resources/sql/user_table.sql` 创建用户表
3. 修改配置文件中的数据库连接信息
4. 运行 `SystemApplication` 启动类
5. 访问接口进行测试

## API文档

启动应用后，可以通过以下地址访问API文档：

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

在Swagger UI中可以直接测试所有API接口，包括：
- 用户登录
- 用户注册
- 获取用户信息

## 测试账号
- 用户名: admin
- 密码: 123456

## 注意事项
- 密码使用 BCrypt 加密存储
- 使用 JWT 进行身份认证
- 支持跨域访问
- 使用逻辑删除
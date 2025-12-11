# 数据库切换指南

本项目现在支持MySQL和PostgreSQL双数据库，通过Spring Profile进行切换。

## 如何切换数据库

### 1. 使用配置文件切换

在`application.yml`中设置激活的Profile：

```yaml
spring:
  profiles:
    active: mysql  # 或 postgresql
```

### 2. 使用环境变量切换

```bash
# 启动时指定MySQL
java -jar -Dspring.profiles.active=mysql your-application.jar

# 启动时指定PostgreSQL
java -jar -Dspring.profiles.active=postgresql your-application.jar
```

### 3. 使用命令行参数切换

```bash
# MySQL
java -jar your-application.jar --spring.profiles.active=mysql

# PostgreSQL
java -jar your-application.jar --spring.profiles.active=postgresql
```

## 数据库配置详情

### MySQL配置文件位置

`src/main/resources/application-mysql.yml`

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/systemDome?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B8
    username: root
    password: 123456
```

### PostgreSQL配置文件位置

`src/main/resources/application-postgresql.yml`

```yaml
spring:
  datasource:
    driver-class-name: org.postgresql.Driver
    url: jdbc:postgresql://localhost:5432/systemdome?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B8
    username: postgres
    password: postgres
```

## 数据库初始化脚本

### MySQL脚本

- `src/main/resources/sql/check_and_init_tables.sql` - MySQL版本的表创建和数据初始化
- `src/main/resources/sql/user_table.sql` - 用户表创建脚本
- `src/main/resources/sql/rbac_init.sql` - RBAC表创建脚本

### PostgreSQL脚本

- `src/main/resources/sql/check_and_init_tables_postgresql.sql` - PostgreSQL版本的表创建和数据初始化

## 注意事项

1. **数据库名称**：
   - MySQL: `systemDome`
   - PostgreSQL: `systemdome`（小写）

2. **连接端口**：
   - MySQL: 3306
   - PostgreSQL: 5432

3. **自动初始化**：
   - 当`app.database.auto-init`设置为`true`时，应用启动时会自动检查并创建缺失的表
   - 系统会根据当前激活的Profile选择对应的初始化脚本

4. **兼容性处理**：
   - 项目已处理MySQL和PostgreSQL之间的主要差异：
     - AUTO_INCREMENT（MySQL）↔ BIGSERIAL（PostgreSQL）
     - ENGINE=InnoDB（MySQL）→ 不适用（PostgreSQL）
     - CHARSET=utf8mb4（MySQL）→ 不适用（PostgreSQL）
     - INSERT IGNORE（MySQL）↔ ON CONFLICT DO NOTHING（PostgreSQL）
     - 反引号`（MySQL）↔ 双引号"（PostgreSQL）
     - UPDATE_TIME触发器（PostgreSQL需要额外创建）

## 测试切换

1. 确保目标数据库已安装并运行
2. 修改对应的数据库配置文件（用户名、密码等）
3. 设置`spring.profiles.active`为所需数据库类型
4. 启动应用

应用启动时会在日志中显示当前使用的数据库类型：
```
INFO  - Database type detected: mysql
```

或

```
INFO  - Database type detected: postgresql
```

## 故障排除

1. **连接失败**：检查数据库服务是否启动、用户名密码是否正确
2. **表不存在**：确认`app.database.auto-init`设置为`true`或手动执行对应的SQL脚本
3. **SQL语法错误**：确认激活的Profile与数据库类型匹配
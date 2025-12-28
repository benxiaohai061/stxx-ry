# Flyway 数据库迁移和多数据库支持

本项目已集成 Flyway 数据库迁移工具，并支持多种数据库（MySQL、H2、PostgreSQL）。

## 功能特性

- **数据库迁移管理**: 使用 Flyway 管理数据库版本和迁移
- **多数据库支持**: 支持 MySQL、H2（测试）、PostgreSQL
- **自动迁移**: 应用启动时自动执行数据库迁移
- **版本控制**: 数据库结构版本化管理

## 支持的数据库

| 数据库 | Profile | 驱动类 | 默认端口 | 适用环境 |
|--------|---------|--------|----------|----------|
| MySQL | `mysql` | `com.mysql.cj.jdbc.Driver` | 3306 | 生产环境 |
| H2 | `h2` | `org.h2.Driver` | - | 测试环境 |
| PostgreSQL | `postgresql` | `org.postgresql.Driver` | 5432 | 生产环境 |

## 配置说明

### 1. 选择数据库类型

在 `application.yml` 中修改激活的 profile：

```yaml
spring:
  profiles:
    active: mysql  # 可选: mysql, h2, postgresql
```

### 2. 数据库连接配置

每个数据库都有对应的配置文件：
- `application-mysql.yml` - MySQL生产环境配置
- `application-h2.yml` - H2测试环境配置
- `application-postgresql.yml` - PostgreSQL生产环境配置

请根据实际环境修改相应配置文件中的数据库连接信息。

**H2数据库特殊说明**：
- 支持内存模式（数据重启后丢失）和文件模式（数据持久化）
- 默认启用H2控制台，可通过 `/h2-console` 访问
- 在测试环境中非常有用，无需额外安装数据库

**Flyway配置说明**：
- 每个数据库profile都会自动扫描对应的迁移脚本目录
- MySQL profile: `classpath:db/migration/mysql`
- PostgreSQL profile: `classpath:db/migration/postgresql`
- H2 profile: `classpath:db/migration/h2`

### 3. Flyway 配置

Flyway 相关配置位于各个数据库配置文件中：

```yaml
flyway:
  enabled: true                    # 启用 Flyway
  locations: classpath:db/migration # 迁移脚本位置
  encoding: UTF-8                  # 脚本编码
  outOfOrder: false               # 是否允许不按顺序迁移
  validateOnMigrate: true         # 迁移时校验
  baselineVersion: 0              # 基线版本
  baselineOnMigrate: true         # 迁移时创建基线
```

## 迁移脚本

### 脚本位置
```
stxx-admin/src/main/resources/db/migration/
├── mysql/          # MySQL数据库脚本
│   └── V1__init.sql
├── postgresql/     # PostgreSQL数据库脚本
│   └── V1__init.sql
└── h2/             # H2数据库脚本
    └── V1__init.sql
```

每个数据库都有独立的目录：
- **mysql/**: MySQL数据库专用的迁移脚本
- **postgresql/**: PostgreSQL数据库专用的迁移脚本
- **h2/**: H2内存数据库专用的迁移脚本

### 命名规范
Flyway 使用以下命名规范：
- `V{版本}__{描述}.sql` - 版本迁移脚本
- `R__{描述}.sql` - 可重复执行的脚本

示例：
- `V1__init.sql` - 初始数据库结构
- `V2__add_user_table.sql` - 添加用户表
- `R__update_permissions.sql` - 更新权限（可重复执行）

### 现有脚本
- `V1__init.sql` - 基于原有 `ry_20250522.sql` 的初始迁移脚本

## 使用方法

### 1. 首次运行
1. 选择合适的数据库类型，修改 `application.yml` 中的 `spring.profiles.active`
2. 配置对应数据库的连接信息
3. 启动应用，Flyway 会自动创建 `flyway_schema_history` 表并执行迁移脚本

### 2. 添加新迁移
1. 在对应的数据库目录下创建新的 SQL 文件：
   - MySQL: `db/migration/mysql/V{版本}__{描述}.sql`
   - PostgreSQL: `db/migration/postgresql/V{版本}__{描述}.sql`
   - H2: `db/migration/h2/V{版本}__{描述}.sql`
2. 遵循命名规范：`V{下一个版本}__{描述}.sql`
3. 如果是跨数据库的通用脚本，需要在每个数据库目录下创建相应版本
4. 重启应用，Flyway 会自动执行对应数据库目录下的迁移脚本

### 3. 切换数据库类型
1. 修改 `application.yml` 中的 profile
2. 配置新数据库的连接信息
3. 确保新数据库中没有数据（或使用不同的数据库名）
4. 启动应用

## 注意事项

### 数据库兼容性
- 迁移脚本需要考虑不同数据库的语法差异
- 对于数据库特定的语法，可以创建多个版本的脚本
- 使用 Flyway 的占位符功能来处理数据库差异

### 数据安全
- **生产环境谨慎操作**：迁移脚本会修改数据库结构
- 建议在测试环境充分验证后再部署到生产
- 备份重要数据

### 版本管理
- 迁移脚本一旦提交到版本控制系统，就不应该修改
- 如需修改，应创建新的迁移脚本来修正

## 常见问题

### Q: 如何回滚迁移？
A: Flyway 不支持自动回滚，需要手动创建新的迁移脚本来撤销更改。

### Q: 如何处理数据库特定的语法？
A: 可以使用 Flyway 的脚本占位符或创建数据库特定的迁移脚本。

### Q: 迁移失败怎么办？
A: 检查迁移脚本语法，确认数据库连接，查看应用日志中的详细错误信息。

## 相关链接

- [Flyway 官方文档](https://flywaydb.org/documentation/)
- [Spring Boot Flyway 集成](https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto.data-access.flyway)

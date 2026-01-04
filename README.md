<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">STXX-RY 企业级快速开发平台</h1>
<h4 align="center">基于 RuoYi 深度优化 | 支持多数据库 | MyBatis-Plus | SpringDoc | Flyway</h4>

<p align="center">
	<img src="https://img.shields.io/badge/Spring%20Boot-2.5.15-brightgreen.svg" alt="Spring Boot">
	<img src="https://img.shields.io/badge/MyBatis--Plus-3.5.2-blue.svg" alt="MyBatis-Plus">
	<img src="https://img.shields.io/badge/Vue-2.6.12-green.svg" alt="Vue">
	<img src="https://img.shields.io/badge/License-MIT-yellow.svg" alt="License">
	<img src="https://img.shields.io/badge/JDK-1.8+-orange.svg" alt="JDK">
</p>

## 📖 项目简介

STXX-RY 是基于若依（RuoYi-Vue）v3.9.1 进行深度改造和优化的企业级快速开发平台，专注于提升开发效率和系统性能。本项目在保留若依核心功能的基础上，集成了当前主流的技术栈和最佳实践。

**适用场景：** 企业管理系统、后台管理平台、SaaS 应用、微服务基础框架

## ✨ 核心优化特性

相比原版 RuoYi，本项目进行了以下重要优化：

### 🚀 性能优化
- **Redis 序列化升级**：将 Redis 序列化方式从默认改为 Jackson，提升序列化性能和可读性
- **MyBatis-Plus 集成**：替换原有单表 SQL 操作，使用 MyBatis-Plus API，减少 80% 的 XML 配置

### 🔌 多数据源支持
- **Dynamic-Datasource 集成**：支持动态多数据源切换，满足复杂业务场景
- **多数据库兼容**：一套代码同时支持 H2、MySQL、PostgreSQL 三种数据库
- **零配置切换**：通过配置文件即可切换数据库类型，无需修改代码

### 📚 API 文档增强
- **SpringDoc + Knife4j**：集成最新的 OpenAPI 3.0 规范文档工具
- **交互式文档**：提供美观易用的 Knife4j UI 界面，支持在线调试

### 🗄️ 数据库版本管理
- **Flyway 集成**：实现数据库版本控制和自动迁移
- **多环境支持**：开发、测试、生产环境数据库脚本统一管理

### 🎯 动态 SQL 功能
- **动态 MyBatis SQL**：支持运行时动态执行 SQL，满足灵活查询需求
- **安全可控**：提供权限控制和 SQL 注入防护

## 🛠️ 技术栈

### 后端技术
| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 2.5.15 | 核心框架 |
| Spring Security | 5.7.14 | 安全框架 |
| MyBatis-Plus | 3.5.2 | ORM 框架 |
| Dynamic-Datasource | 3.5.2 | 多数据源 |
| SpringDoc | 1.7.0 | API 文档 |
| Knife4j | 4.4.0 | API UI |
| Flyway | 8.5.13 | 数据库迁移 |
| Redis | - | 缓存中间件 |
| JWT | 0.9.1 | 认证令牌 |
| Hutool | 5.8.38 | 工具类库 |

### 前端技术
| 技术 | 版本 | 说明 |
|------|------|------|
| Vue | 2.6.12 | 前端框架 |
| Element UI | 2.15.14 | UI 组件库 |
| Axios | 0.28.1 | HTTP 客户端 |
| Vuex | 3.6.0 | 状态管理 |
| Vue Router | 3.4.9 | 路由管理 |

### 数据库支持
- **MySQL** 5.7+ / 8.0+
- **PostgreSQL** 12+
- **H2** 2.1+ (内存数据库，适合开发测试)

## 📦 快速开始

### 环境要求
- JDK 1.8+
- Maven 3.6+
- Node.js 12+
- Redis 3.0+
- MySQL 5.7+ / PostgreSQL 12+ / H2 (任选其一)

### 后端启动

```bash
# 克隆项目
git clone https://github.com/benxiaohai061/stxx-ry.git

# 进入项目目录
cd stxx-ry

# 编译打包
mvn clean install

# 启动项目
cd stxx-admin
mvn spring-boot:run
```

### 前端启动

```bash
# 进入前端目录
cd stxx-ui

# 安装依赖
pnpm install

# 启动开发服务器
pnpm run dev
```

### 数据库配置

项目支持三种数据库，在 `application.yml` 中配置：

**使用 MySQL：**
```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/ry-vue?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8
```

**使用 PostgreSQL：**
```yaml
spring:
  datasource:
    driver-class-name: org.postgresql.Driver
    url: jdbc:postgresql://localhost:5432/ry-vue
```

**使用 H2（开发测试）：**
```yaml
spring:
  datasource:
    driver-class-name: org.h2.Driver
    url: jdbc:h2:mem:testdb
```

### 访问地址
- 前端地址：http://localhost:80
- 后端接口：http://localhost:8080
- API 文档：http://localhost:8080/doc.html
- 默认账号：admin / admin123

## 🎯 核心功能

### 系统管理
- **用户管理**：系统用户配置、用户信息维护
- **部门管理**：组织机构管理（公司、部门、小组），树形结构展示，支持数据权限
- **岗位管理**：用户岗位职务配置
- **菜单管理**：系统菜单配置、操作权限、按钮权限标识
- **角色管理**：角色权限分配、数据范围权限划分
- **字典管理**：系统常用固定数据维护
- **参数管理**：系统动态参数配置

### 系统监控
- **在线用户**：实时监控当前活跃用户状态
- **定时任务**：在线任务调度管理（添加、修改、删除），包含执行结果日志
- **服务监控**：监控系统 CPU、内存、磁盘、堆栈等信息
- **缓存监控**：Redis 缓存信息查询、命令统计
- **连接池监控**：数据库连接池状态监控、SQL 性能分析

### 日志管理
- **操作日志**：系统操作日志记录和查询
- **登录日志**：用户登录日志记录，包含登录异常

### 开发工具
- **代码生成**：一键生成前后端代码（Java、HTML、XML、SQL），支持 CRUD 下载
- **系统接口**：基于 SpringDoc + Knife4j 的 API 文档，支持在线调试
- **在线构建器**：拖拽式表单设计器，生成对应 HTML 代码
- **动态 SQL**：支持运行时动态执行 MyBatis SQL

### 消息通知
- **通知公告**：系统公告信息发布和维护

## 💡 为什么选择 STXX-RY？

### 对比原版 RuoYi 的优势

| 特性 | 原版 RuoYi | STXX-RY |
|------|-----------|---------|
| ORM 框架 | MyBatis | MyBatis-Plus（减少 80% XML） |
| 数据库支持 | MySQL | MySQL + PostgreSQL + H2 |
| 多数据源 | ❌ | ✅ Dynamic-Datasource |
| API 文档 | Swagger 2 | SpringDoc + Knife4j (OpenAPI 3.0) |
| 数据库迁移 | 手动执行 SQL | Flyway 自动迁移 |
| Redis 序列化 | JDK 序列化 | Jackson（性能更优） |
| 动态 SQL | ❌ | ✅ 支持运行时动态执行 |

### 适用人群
- 需要快速搭建企业管理系统的开发团队
- 需要支持多数据库的 SaaS 应用开发者
- 希望使用 MyBatis-Plus 简化开发的 Java 工程师
- 需要规范化数据库版本管理的项目团队

## 📂 项目结构

```
stxx-ry
├── stxx-admin          # 启动模块（主应用入口）
├── stxx-common         # 通用工具模块
├── stxx-framework      # 框架核心模块
├── stxx-generator      # 代码生成模块
├── stxx-quartz         # 定时任务模块
├── stxx-system         # 系统管理模块
├── stxx-ui             # 前端 Vue 项目
└── sql                 # 数据库脚本（支持 MySQL/PostgreSQL/H2）
```

## 🔧 配置说明

### MyBatis-Plus 配置
项目已集成 MyBatis-Plus，所有单表 CRUD 操作无需编写 XML：

```java
// 继承 BaseMapper 即可使用
public interface UserMapper extends BaseMapper<User> {
    // 自动拥有 insert、update、delete、selectById 等方法
}
```

### 多数据源配置
使用 `@DS` 注解切换数据源：

```java
@Service
public class UserService {
    
    @DS("master")  // 主库
    public void saveUser(User user) { }
    
    @DS("slave")   // 从库
    public User getUser(Long id) { }
}
```

### Flyway 数据库迁移
数据库脚本放在 `resources/db/migration` 目录：
```
db/migration/
├── V1.0__init_schema.sql
├── V1.1__add_user_table.sql
└── V1.2__add_role_table.sql
```

启动时自动执行未执行的迁移脚本。

## 🚀 部署指南

### Docker 部署

```bash
# 构建镜像
docker build -t stxx-ry:latest .

# 运行容器
docker run -d -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/ry-vue \
  -e SPRING_REDIS_HOST=redis \
  stxx-ry:latest
```

### JAR 包部署

```bash
# 打包
mvn clean package -DskipTests

# 运行
java -jar stxx-admin/target/stxx-admin.jar \
  --spring.datasource.url=jdbc:mysql://localhost:3306/ry-vue \
  --spring.redis.host=localhost
```

## 📝 开发指南

### API 文档使用
访问 http://localhost:8080/doc.html 查看完整 API 文档，支持：
- 在线调试接口
- 查看请求/响应示例
- 导出 OpenAPI JSON/YAML

## 🤝 贡献指南

欢迎提交 Issue 和 Pull Request！

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 提交 Pull Request

## 📄 开源协议

本项目基于 [MIT License](LICENSE) 开源协议。

## 🙏 致谢

本项目基于 [RuoYi-Vue](https://gitee.com/y_project/RuoYi-Vue) 进行改造，感谢若依团队的开源贡献。

## 📮 联系方式

如有问题或建议，欢迎通过以下方式联系：
- 提交 Issue
- 发送邮件至：wangccwork@163.com

---

⭐ 如果这个项目对你有帮助，请给个 Star 支持一下！

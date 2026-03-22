# ManagementPlat - SAP统一管理平台

## 项目概述

SAP统一管理平台是一个面向企业的综合管理系统，支持多SAP系统监控、应用管理、第三方系统集成等功能。

### 核心功能

| 模块 | 功能 | 说明 |
|------|------|------|
| **监控模块** | SAP系统监控 | 多系统选择、指标图表展示、时间范围调整 |
| | SQL配置管理 | 可视化SQL配置、参数化查询、缓存策略 |
| | 第三方嵌入 | iframe集成第三方监控平台 |
| **应用管理** | 应用系统管理 | CRUD操作、表单定制、自定义按钮 |
| | SAP集成 | HANA直连、WebService调用 |
| **系统管理** | 用户管理 | 本地用户注册/登录、OAuth2.0第三方登录 |
| | 角色权限 | 完整RBAC权限控制 |
| | OAuth2配置 | 钉钉/通用OAuth2可配置集成 |

---

## 技术架构

### 技术栈

| 层级 | 技术 | 版本 |
|------|------|------|
| **后端** | Spring Boot | 3.2.x |
| | Spring Security | 6.x |
| | MyBatis Plus | 3.5.x |
| | JWT | 0.12.x |
| **前端** | Vue3 | 3.4.x |
| | Vite | 5.x |
| | Element Plus | 2.5.x |
| | ECharts | 5.x |
| | Pinia | 2.x |
| **数据库** | MySQL | 8.0 |
| | SAP HANA | (直连) |
| **部署** | Docker | 24.x |
| | Nginx | latest |

### 系统架构图

```
┌─────────────────────────────────────────────────────────────────┐
│                        前端 (Vue3 + Vite)                        │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────────────┐  │
│  │ 监控模块     │  │ 应用管理     │  │ 中间件纳管           │  │
│  │ (ECharts)   │  │ (CRUD Form)  │  │ (iframe集成)         │  │
│  └──────────────┘  └──────────────┘  └──────────────────────┘  │
└────────────────────────────┬────────────────────────────────────┘
                             │ HTTP/REST
┌────────────────────────────┴────────────────────────────────────┐
│                      后端 (SpringBoot 3.x)                        │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────────────┐  │
│  │ 认证模块     │  │ 监控服务     │  │ 业务管理服务         │  │
│  │ (JWT/OAuth2) │  │ (HANA查询)   │  │ (CRUD+业务逻辑)      │  │
│  └──────────────┘  └──────────────┘  └──────────────────────┘  │
└────────────────────────────┬────────────────────────────────────┘
                             │
          ┌──────────────────┼──────────────────┐
          ▼                  ▼                  ▼
    ┌──────────┐       ┌──────────┐       ┌──────────┐
    │  MySQL   │       │ SAP HANA │       │ 第三方API│
    │ (元数据) │       │ (业务库) │       │ (WebService)│
    └──────────┘       └──────────┘       └──────────┘
```

---

## 项目结构

```
ManagementPlat/
│
├── backend/                          # SpringBoot 后端
│   ├── src/main/java/com/platform/
│   │   ├── PlatformApplication.java
│   │   ├── config/                   # 配置类
│   │   │   ├── SecurityConfig.java
│   │   │   ├── CorsConfig.java
│   │   │   └── HanaDataSourceConfig.java
│   │   ├── auth/                     # 认证模块
│   │   │   ├── controller/
│   │   │   ├── service/
│   │   │   ├── security/
│   │   │   └── dto/
│   │   ├── system/                   # 系统管理
│   │   │   ├── controller/
│   │   │   ├── service/
│   │   │   ├── mapper/
│   │   │   └── model/
│   │   ├── monitor/                   # 监控模块
│   │   │   ├── controller/
│   │   │   ├── service/
│   │   │   ├── mapper/
│   │   │   └── model/
│   │   ├── application/               # 应用管理
│   │   │   ├── controller/
│   │   │   ├── service/
│   │   │   ├── mapper/
│   │   │   └── model/
│   │   └── integrate/                 # 第三方集成
│   │       ├── controller/
│   │       ├── service/
│   │       ├── mapper/
│   │       └── model/
│   ├── src/main/resources/
│   │   ├── mapper/                    # MyBatis XML
│   │   └── application.yml
│   └── pom.xml
│
├── frontend/                         # Vue3 前端
│   ├── src/
│   │   ├── api/                      # API调用
│   │   ├── components/               # 组件
│   │   ├── views/                    # 页面
│   │   ├── router/                   # 路由
│   │   ├── stores/                   # 状态管理
│   │   └── styles/                   # 样式
│   ├── package.json
│   └── vite.config.js
│
├── database/                         # 数据库
│   └── init.sql                      # 初始化脚本
│
├── docker/                          # Docker部署
│   ├── docker-compose.yml
│   ├── mysql/
│   └── nginx/
│
└── docs/                            # 文档
    ├── README.md
    ├── ARCHITECTURE.md              # 架构设计
    ├── API.md                        # 接口文档
    └── DEPLOYMENT.md                 # 部署文档
```

---

## 数据库设计

### 核心表结构

| 表名 | 说明 |
|------|------|
| `sys_user` | 用户表 |
| `sys_role` | 角色表 |
| `sys_permission` | 权限表 |
| `sys_user_role` | 用户角色关联 |
| `sys_role_permission` | 角色权限关联 |
| `oauth2_provider_config` | OAuth2提供商配置 |
| `oauth2_binding` | OAuth2用户绑定 |
| `sap_system` | SAP系统配置 |
| `sap_system_action` | SAP系统自定义操作 |
| `monitor_sql_config` | 监控SQL配置 |
| `monitor_sql_log` | SQL执行日志 |
| `monitor_metrics_cache` | 监控指标缓存 |
| `application` | 应用配置 |
| `integrate_platform` | 第三方平台配置 |
| `integrate_access_log` | 集成访问日志 |

---

## API接口设计

### 认证模块 `/api/auth/*`

| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/auth/login` | POST | 用户名密码登录 |
| `/api/auth/register` | POST | 用户注册 |
| `/api/auth/forgot-password` | POST | 忘记密码 |
| `/api/auth/refresh-token` | POST | 刷新Token |
| `/api/auth/oauth2/{provider}` | GET | OAuth2跳转 |
| `/api/auth/oauth2/{provider}/callback` | GET | OAuth2回调 |

### 系统管理 `/api/system/*`

| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/system/user` | GET/POST/PUT/DELETE | 用户CRUD |
| `/api/system/role` | GET/POST/PUT/DELETE | 角色CRUD |
| `/api/system/permission` | GET | 权限列表 |

### 监控模块 `/api/monitor/*`

| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/monitor/systems` | GET | SAP系统列表 |
| `/api/monitor/metrics` | GET | 获取指标数据 |
| `/api/monitor/sql-config` | GET/POST/PUT/DELETE | SQL配置管理 |
| `/api/monitor/sql-execute` | POST | 执行SQL预览 |

### 应用管理 `/api/application/*`

| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/application` | GET/POST/PUT/DELETE | 应用CRUD |
| `/api/application/{id}/action/{actionCode}` | POST | 执行自定义操作 |

### 第三方集成 `/api/integrate/*`

| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/integrate/platforms` | GET/POST/PUT/DELETE | 平台配置管理 |
| `/api/integrate/sso-token` | GET | 生成SSO Token |
| `/api/integrate/iframe-url` | GET | 获取iframe访问URL |

---

## 配置说明

### 环境变量

| 变量 | 说明 | 默认值 |
|------|------|--------|
| `DB_HOST` | MySQL地址 | localhost |
| `DB_PORT` | MySQL端口 | 3306 |
| `DB_NAME` | 数据库名 | manageplat |
| `DB_USERNAME` | 数据库用户名 | root |
| `DB_PASSWORD` | 数据库密码 | root123 |
| `JWT_SECRET` | JWT密钥 | (需修改) |
| `HANA_HOST` | HANA主机 | localhost |
| `HANA_PORT` | HANA端口 | 30015 |

---

## 开发指南

### 后端开发

```bash
# 1. 安装依赖
cd backend
mvn clean install

# 2. 本地运行
mvn spring-boot:run

# 3. 运行测试
mvn test

# 4. 单个测试
mvn test -Dtest=AuthServiceTest
```

### 前端开发

```bash
# 1. 安装依赖
cd frontend
npm install

# 2. 开发模式
npm run dev

# 3. 构建生产版本
npm run build

# 4. Lint检查
npm run lint
```

### Docker部署

```bash
# 启动所有服务
docker-compose up -d

# 查看日志
docker-compose logs -f

# 停止服务
docker-compose down
```

---

## 版本历史

| 版本 | 日期 | 说明 |
|------|------|------|
| 1.0.0 | 2026-03 | 初始版本，基础框架搭建 |

---

## 项目进度

### ✅ Phase 1: 基础框架 (已完成)

| 模块 | 状态 | 说明 |
|------|------|------|
| 项目结构搭建 | ✅ | 前后端目录结构 |
| 数据库设计 | ✅ | 16张核心表 + 初始数据 |
| 后端框架 | ✅ | SpringBoot 3.2 + MyBatis Plus |
| 前端框架 | ✅ | Vue3 + Element Plus |
| 认证模块 | ✅ | JWT + 本地登录 |
| 系统管理 | ✅ | 用户/角色/权限 CRUD |
| 监控模块 | ✅ | SAP配置 + SQL配置 + HANA查询 |
| 第三方集成 | ✅ | 平台配置 + SSO Token |
| Docker部署 | ✅ | docker-compose编排 |
| 文档 | ✅ | README + ARCHITECTURE + DEPLOYMENT |

### 📋 Phase 2-6: 功能完善 (待开发)

| 模块 | 功能 | 优先级 |
|------|------|--------|
| OAuth2 | 钉钉登录集成 | P1 |
| OAuth2 | 通用OAuth2登录 | P1 |
| Monitor | HANA连接管理界面 | P1 |
| Monitor | SQL配置管理界面 | P1 |
| Monitor | 监控图表展示 | P1 |
| Integrate | 第三方平台管理界面 | P2 |
| Integrate | iframe嵌入组件 | P2 |
| Application | 应用管理功能 | P2 |
| Application | 自定义按钮执行 | P2 |

### 📊 文件统计

```
总文件数: 79
├── Java文件: 59
├── Vue文件: 7
├── JS文件: 6
└── 配置文件: 7
```

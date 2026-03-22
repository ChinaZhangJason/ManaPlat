# AGENTS.md - ManagementPlat 开发指南

SAP统一管理平台，Vue3 + SpringBoot 前后端分离架构，支持响应式布局、可配置报表、告警管理、SAP连接配置。

## 项目结构

```
backend/              # SpringBoot 3.2 (Java 17)
  src/main/java/com/platform/
    common/          # Result, Constants, Exception, Util
    config/         # Security, Cors, Mybatis, Jackson, Async
    interceptor/    # RequestInterceptor
    auth/           # 登录/JWT/OAuth2/验证码/密码重置
    system/         # 用户/角色/权限/菜单
    monitor/       # 监控/报表/告警/SAP连接
    alert/         # 告警规则/接收人/历史/通知
    sap/           # SAP RFC/API/WebService/HANA连接
    test/          # API测试/SQL测试
    workflow/      # 工作流(状态机)
    application/   # 应用管理
    integrate/     # 第三方集成/SSO
frontend/          # Vue3 + Element Plus + ECharts
  src/
    api/           # API封装 (9个模块)
    components/
      layout/      # 响应式布局组件
      charts/     # 8种图表组件
    views/         # 页面 (20个)
    stores/        # Pinia状态 (app, user)
    utils/         # 工具函数
    directives/    # 权限指令
    i18n/         # 国际化
database/          # init.sql, migration_v2.sql
docker/            # Docker部署
docs/              # 文档
```

## 构建命令

### 后端 (Maven)
```bash
cd backend
mvn clean package -DskipTests      # 编译打包
mvn spring-boot:run                # 开发运行 (端口8080)
mvn test                            # 运行所有测试
mvn test -Dtest=AuthServiceTest    # 运行单个测试类
mvn test -Dtest=AuthServiceTest#testLogin  # 运行单个测试方法
mvn test jacoco:report             # 生成覆盖率报告
```

### 前端 (Vite)
```bash
cd frontend
npm install                         # 安装依赖
npm run dev                         # 开发模式 (localhost:5173)
npm run build                       # 生产构建
npm run preview                     # 预览构建
npm test                            # 运行测试 (Vitest)
npm run test:coverage              # 覆盖率报告
```

### Docker
```bash
cd docker && docker-compose up -d --build
```

---

## TDD测试驱动开发

每个模块开发遵循TDD流程：

```
1. 编写测试用例 (RED)      → 测试必须先写
2. 实现功能代码 (GREEN)     → 让测试通过
3. 重构优化 (REFACTOR)     → 优化代码
4. 验证通过后继续下一个任务
```

### 测试覆盖率要求
- 后端Service层: 80%+ 覆盖率
- 前端组件: 关键方法测试
- E2E: 每个页面关键操作全覆盖

---

## 代码规范

### Java (Spring Boot)

**命名:**
- 类名: PascalCase (`UserService`)
- 方法/变量: camelCase (`getUserById`)
- 常量: UPPER_SNAKE_CASE (`MAX_RETRY`)
- 包名: 全小写 (`com.platform.common`)

**包结构:**
```
controller   → HTTP请求处理
service/impl → 业务逻辑实现
mapper      → 数据访问 (MyBatis)
model       → 实体类
dto         → 数据传输对象
```

**类命名:**
- Controller: `XxxController.java`
- Service: `XxxService.java` + `XxxServiceImpl.java`
- Mapper: `XxxMapper.java`
- Entity: `Xxx.java`

**异常处理:**
```java
return Result.error(500, "错误信息");
throw new BusinessException("业务错误");
```

---

### JavaScript/Vue

**命名:**
- 组件: PascalCase (`UserList.vue`)
- 变量/函数: camelCase
- CSS类: kebab-case

**Vue组件:**
```vue
<template>
  <div class="component-name">
    <!-- 组件内容 -->
  </div>
</template>

<script setup>
// 逻辑
</script>

<style scoped>
.component-name { /* 样式 */ }
</style>
```

**图表组件 (8种):**
```
LineChart.vue      # 折线图
BarChart.vue       # 柱状图
PieChart.vue      # 饼图
ScatterChart.vue   # 散点图
GaugeChart.vue     # 仪表盘
TableChart.vue     # 表格
RadarChart.vue     # 雷达图
HeatmapChart.vue   # 热力图
```

---

## API设计

**统一响应:**
```json
{ "code": 200, "message": "success", "data": {}, "timestamp": 1699999999 }
```

**路径规范:**
```
GET    /api/{module}/list
GET    /api/{module}/{id}
POST   /api/{module}
PUT    /api/{module}/{id}
DELETE /api/{module}/{id}
```

---

## 数据库

**命名:** 小写下划线 (`sys_user`, `created_at`)
**索引:** `idx_字段名` / `uk_字段名`

---

## Git提交

```
<type>(<scope>): <subject>

feat(auth): 添加登录功能
fix(monitor): 修复SQL超时
refactor(report): 重构报表组件
test(alert): 添加告警测试用例
```

类型: feat, fix, docs, style, refactor, test, chore

---

## 模块说明

### 报表系统 (monitor/report)
- 可配置化报表，每个报表独立文件
- 数据源: SQL/HANA/API/WebSocket/文件
- 图表: 8种ECharts图表
- 支持拖拽调整大小/位置

### 告警系统 (alert)
- 告警规则配置
- 接收人管理 (邮件/短信)
- 告警历史
- 阈值条件: GT/LT/EQ/GE/LE/BETWEEN

### SAP连接 (sap)
- RFC连接配置
- API配置 (REST/OData/Graph)
- WebService配置
- HANA连接配置

### 测试工具 (test)
- API测试页面
- SQL测试页面
- 历史记录管理

### 工作流 (workflow) - 预留
- 简单审批流 (状态机模式)
- 流程定义/实例/任务

### 响应式布局
- 桌面/平板/手机自适应
- 菜单: 折叠/固定/图标模式
- 权限指令: `v-permission`

### 密码重置 (auth)
- 手机验证码重置
- 邮箱验证码重置
- 验证码10分钟有效

---

## 测试文件清单

### 后端测试 (15个)
```
backend/src/test/java/com/platform/
├── ManagementPlatApplicationTest.java
├── ApiIntegrationTest.java
├── common/
│   ├── ResultTest.java
│   └── BusinessExceptionTest.java
├── auth/
│   ├── AuthServiceTest.java
│   └── AuthDtoTest.java
├── system/
│   └── SysModelTest.java
├── monitor/
│   ├── ReportServiceTest.java
│   └── DataSourceServiceTest.java
├── alert/
│   ├── AlertServiceTest.java
│   └── AlertRuleTest.java
├── sap/
│   └── SapConfigTest.java
├── workflow/
│   ├── WorkflowServiceTest.java
│   └── WorkflowTest.java
└── integrate/
    └── IntegrateTest.java
```

### 前端测试 (7个)
```
frontend/src/__tests__/
├── app.spec.js
├── user.spec.js
├── chart.spec.js
├── alert.spec.js
├── api.spec.js
├── permission.spec.js
└── system.spec.js
```

---

## 数据库表清单

### 用户权限 (5表)
- sys_user, sys_role, sys_permission
- sys_user_role, sys_role_permission

### 报表模块 (2表)
- monitor_report_config
- monitor_report_widget

### 告警模块 (4表)
- monitor_alert_rule
- monitor_alert_receiver
- monitor_alert_rule_receiver
- monitor_alert_history

### SAP连接 (5表)
- sap_rfc_config
- sap_rfc_function
- sap_api_config
- sap_webservice_config
- hana_connection_config

### 工作流 (3表)
- workflow_definition
- workflow_instance
- workflow_task

### 其他 (4表)
- system_menu
- verify_code
- system_oper_log
- integrate_platform

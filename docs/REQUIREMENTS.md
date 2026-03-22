# 需求规格说明书

## 一、项目概述

**项目名称**: SAP统一管理平台 (ManagementPlat)
**项目类型**: 企业级Web应用
**核心功能**: 统一管理SAP系统、监控指标、报表展示、告警通知、第三方集成
**目标用户**: 企业IT运维人员、系统管理员、业务分析师

---

## 二、技术架构

### 2.1 技术栈

| 层级 | 技术选型 | 版本 |
|------|----------|------|
| 前端 | Vue3 + Vite | 3.4+ / 5.0+ |
| UI框架 | Element Plus | 2.5+ |
| 图表库 | ECharts | 5.5+ |
| 状态管理 | Pinia | 2.1+ |
| 后端 | Spring Boot | 3.2+ |
| 开发语言 | Java | 17 |
| ORM | MyBatis Plus | 3.5+ |
| 安全框架 | Spring Security | 6.x |
| 认证 | JWT | 0.12+ |
| 数据库 | MySQL | 8.0+ |

### 2.2 系统架构

```
┌─────────────────────────────────────────────────────────────┐
│                        用户浏览器                            │
├─────────────────────────────────────────────────────────────┤
│                      Nginx (反向代理)                        │
│                    Port: 80/443                              │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  ┌──────────────────┐         ┌──────────────────┐          │
│  │   Frontend        │         │   Backend        │          │
│  │   Vue3            │         │   Spring Boot    │          │
│  │   Port: 5173     │ <────-> │   Port: 8080     │          │
│  └──────────────────┘         └────────┬─────────┘          │
│                                        │                     │
│                              ┌─────────┴─────────┐           │
│                              │                    │           │
│                      ┌───────┴───────┐   ┌──────┴──────┐     │
│                      │    MySQL      │   │   外部服务   │     │
│                      │   Port:3306   │   │             │     │
│                      └───────────────┘   │  - SAP HANA │     │
│                                         │  - SAP RFC  │     │
│                                         │  - REST API │     │
│                                         │  - WebService│    │
│                                         └─────────────┘     │
└─────────────────────────────────────────────────────────────────┘
```

---

## 三、功能需求

### 3.1 响应式布局 (P0)

**需求描述**:
- 支持桌面、平板、手机三种设备自适应
- 左侧菜单支持：隐藏、固定、图标模式
- 菜单可配置显示/隐藏

**验收标准**:
- [ ] 桌面端(>1200px): 完整侧边菜单，固定显示
- [ ] 平板端(768-1200px): 可折叠侧边菜单
- [ ] 手机端(<768px): 汉堡菜单，侧边抽屉
- [ ] 菜单切换动画流畅

### 3.2 仪表盘报表系统 (P0)

**需求描述**:
- 每个报表单独一个开发文件
- 报表可配置化：数据源、指标、图表类型
- 支持多种数据源：SQL、HANA、REST API、WebSocket、文件
- 支持过滤器：日期时间、系统、自定义维度
- 支持多种图表：折线图、柱状图、散点图、饼图、仪表盘、雷达图、热力图等

**验收标准**:
- [ ] 能创建新报表并保存配置
- [ ] 报表能从多种数据源获取数据
- [ ] 图表类型可切换
- [ ] 过滤器生效，数据动态更新
- [ ] 报表支持拖拽调整大小/位置

### 3.3 API测试 + SQL测试 (P1)

**需求描述**:
- 提供API测试页面：构建请求、发送、执行历史
- 提供SQL测试页面：编写SQL、执行、结果展示
- 测试结果支持导出

**验收标准**:
- [ ] API测试：GET/POST/PUT/DELETE请求正常
- [ ] SQL测试：查询能执行，结果正确展示
- [ ] 历史记录能查看和回放

### 3.4 第三方页面嵌入 (P1)

**需求描述**:
- 支持iframe嵌入外部页面
- SSO Token自动注入
- 支持事件传递（可选）

**验收标准**:
- [ ] 外部页面能正常嵌入
- [ ] SSO登录正常
- [ ] 页面自适应容器大小

### 3.5 RBAC权限配置 (P0)

**需求描述**:
- 菜单权限配置界面
- 按钮权限控制
- 角色权限分配
- 数据权限（可选）

**验收标准**:
- [ ] 能分配角色权限
- [ ] 不同角色看到不同菜单
- [ ] 按钮根据权限显示/隐藏

### 3.6 用户密码自助初始化 (P1)

**需求描述**:
- 支持手机号验证码重置
- 支持邮箱验证码重置
- 验证码有效期控制

**验收标准**:
- [ ] 手机号能收到验证码
- [ ] 邮箱能收到验证码
- [ ] 验证码能正确重置密码

### 3.7 告警配置 (P1)

**需求描述**:
- 告警规则配置：阈值、条件、触发方式
- 告警接收人管理：用户组、接收方式
- 通知服务：邮件、短信
- 告警历史记录

**验收标准**:
- [ ] 能创建告警规则
- [ ] 能配置接收人
- [ ] 告警触发后能收到通知

### 3.8 应用管理系统配置 (P0)

**需求描述**:
- 纳管系统类型：SAP ABAP、SAP JAVA、HANA、中间件
- 系统连接配置
- 系统分组管理

**验收标准**:
- [ ] 能添加各类型系统配置
- [ ] 连接参数能保存
- [ ] 能测试连接

### 3.9 SAP连接配置 (新增)

**需求描述**:
- RFC接口配置管理
- REST API接口配置管理
- Webservice接口配置管理
- HANA数据库连接配置

**验收标准**:
- [ ] 能配置RFC连接参数
- [ ] 能配置API端点
- [ ] 能配置Webservice
- [ ] 能配置HANA连接
- [ ] 能测试连接

### 3.10 工作流预留 (P2)

**需求描述**:
- 简单审批流（状态机模式）
- 预留接口和表结构
- 流程定义、实例管理

**验收标准**:
- [ ] 表结构已创建
- [ ] 基础接口已实现

---

## 四、数据需求

### 4.1 新增数据库表

#### 报表相关
```sql
-- 报表配置表
CREATE TABLE monitor_report_config (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    report_name VARCHAR(100) NOT NULL COMMENT '报表名称',
    report_code VARCHAR(50) UNIQUE NOT NULL COMMENT '报表编码',
    description TEXT COMMENT '描述',
    layout_config JSON COMMENT '布局配置',
    filter_config JSON COMMENT '过滤器配置',
    refresh_interval INT DEFAULT 60 COMMENT '刷新间隔(秒)',
    status TINYINT DEFAULT 1 COMMENT '状态 0禁用 1启用',
    created_by BIGINT COMMENT '创建人',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 报表组件表
CREATE TABLE monitor_report_widget (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    report_id BIGINT NOT NULL COMMENT '报表ID',
    widget_name VARCHAR(100) NOT NULL COMMENT '组件名称',
    widget_type VARCHAR(50) NOT NULL COMMENT '组件类型 LINE/BAR/PIE/...',
    data_source_type VARCHAR(20) NOT NULL COMMENT '数据源类型 SQL/HANA/API/WS',
    data_source_config JSON NOT NULL COMMENT '数据源配置',
    chart_config JSON COMMENT '图表配置',
    position_config JSON COMMENT '位置尺寸配置',
    sort_order INT DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (report_id) REFERENCES monitor_report_config(id)
);
```

#### 告警相关
```sql
-- 告警规则表
CREATE TABLE monitor_alert_rule (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    rule_name VARCHAR(100) NOT NULL COMMENT '规则名称',
    rule_code VARCHAR(50) UNIQUE NOT NULL COMMENT '规则编码',
    metric_type VARCHAR(50) NOT NULL COMMENT '指标类型',
    condition_type VARCHAR(20) NOT NULL COMMENT '条件类型 GT/LT/EQ/BETWEEN',
    threshold_value VARCHAR(100) COMMENT '阈值',
    threshold_value_max VARCHAR(100) COMMENT '最大值(区间类型)',
    time_window INT DEFAULT 300 COMMENT '时间窗口(秒)',
    system_id BIGINT COMMENT '关联系统ID',
    status TINYINT DEFAULT 1 COMMENT '状态',
    created_by BIGINT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 告警接收人表
CREATE TABLE monitor_alert_receiver (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    receiver_name VARCHAR(100) NOT NULL COMMENT '接收人名称',
    receiver_type VARCHAR(20) NOT NULL COMMENT '接收类型 USER/GROUP',
    user_id BIGINT COMMENT '用户ID',
    group_name VARCHAR(100) COMMENT '用户组名称',
    notify_channels JSON NOT NULL COMMENT '通知渠道 EMAIL/SMS/WEBHOOK',
    notify_config JSON COMMENT '通知配置',
    status TINYINT DEFAULT 1,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 告警规则-接收人关联表
CREATE TABLE monitor_alert_rule_receiver (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    rule_id BIGINT NOT NULL,
    receiver_id BIGINT NOT NULL,
    FOREIGN KEY (rule_id) REFERENCES monitor_alert_rule(id),
    FOREIGN KEY (receiver_id) REFERENCES monitor_alert_receiver(id)
);

-- 告警历史表
CREATE TABLE monitor_alert_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    rule_id BIGINT NOT NULL,
    rule_name VARCHAR(100),
    alert_level VARCHAR(20) DEFAULT 'WARNING' COMMENT 'WARNING/ERROR/CRITICAL',
    metric_value VARCHAR(100) COMMENT '触发时的指标值',
    alert_message TEXT,
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT 'PENDING/SENT/ACKED/CLOSED',
    sent_at DATETIME,
    acked_by BIGINT,
    acked_at DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

#### SAP连接配置 (新增)
```sql
-- SAP RFC连接配置表
CREATE TABLE sap_rfc_config (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    config_name VARCHAR(100) NOT NULL COMMENT '配置名称',
    config_code VARCHAR(50) UNIQUE NOT NULL COMMENT '配置编码',
    system_id BIGINT NOT NULL COMMENT '关联SAP系统ID',
    connection_type VARCHAR(20) NOT NULL COMMENT '连接类型 DIRECT/JCO',
    host VARCHAR(100) COMMENT '主机地址',
    port INT COMMENT '端口',
    client VARCHAR(10) COMMENT '客户端',
    lang VARCHAR(10) DEFAULT 'EN',
    system_number VARCHAR(10) COMMENT '系统编号',
    pool_size INT DEFAULT 5 COMMENT '连接池大小',
    connection_config JSON COMMENT '其他连接配置',
    status TINYINT DEFAULT 1,
    created_by BIGINT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- RFC函数配置表
CREATE TABLE sap_rfc_function (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    rfc_config_id BIGINT NOT NULL COMMENT 'RFC连接ID',
    function_name VARCHAR(100) NOT NULL COMMENT '函数名称',
    function_desc VARCHAR(200) COMMENT '函数描述',
    import_params JSON COMMENT '输入参数定义',
    export_params JSON COMMENT '输出参数定义',
    tables_params JSON COMMENT '表参数定义',
    status TINYINT DEFAULT 1,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (rfc_config_id) REFERENCES sap_rfc_config(id)
);

-- SAP API接口配置表
CREATE TABLE sap_api_config (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    config_name VARCHAR(100) NOT NULL COMMENT '配置名称',
    config_code VARCHAR(50) UNIQUE NOT NULL COMMENT '配置编码',
    system_id BIGINT NOT NULL COMMENT '关联SAP系统ID',
    api_type VARCHAR(50) NOT NULL COMMENT 'API类型 OData/REST/Graph',
    base_url VARCHAR(255) NOT NULL COMMENT 'API基础地址',
    auth_type VARCHAR(20) NOT NULL COMMENT '认证类型 BASIC/OAUTH2/CERT',
    auth_config JSON COMMENT '认证配置',
    timeout INT DEFAULT 30000 COMMENT '超时时间(ms)',
    retry_count INT DEFAULT 3 COMMENT '重试次数',
    headers JSON COMMENT '自定义请求头',
    status TINYINT DEFAULT 1,
    created_by BIGINT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- SAP WebService配置表
CREATE TABLE sap_webservice_config (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    config_name VARCHAR(100) NOT NULL COMMENT '配置名称',
    config_code VARCHAR(50) UNIQUE NOT NULL COMMENT '配置编码',
    system_id BIGINT NOT NULL COMMENT '关联SAP系统ID',
    wsdl_url VARCHAR(500) NOT NULL COMMENT 'WSDL地址',
    service_name VARCHAR(100) COMMENT '服务名称',
    port_name VARCHAR(100) COMMENT '端口名称',
    endpoint_url VARCHAR(255) COMMENT '端点地址',
    auth_type VARCHAR(20) DEFAULT 'NONE' COMMENT '认证类型',
    auth_config JSON COMMENT '认证配置',
    status TINYINT DEFAULT 1,
    created_by BIGINT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- HANA数据库连接配置表
CREATE TABLE hana_connection_config (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    config_name VARCHAR(100) NOT NULL COMMENT '配置名称',
    config_code VARCHAR(50) UNIQUE NOT NULL COMMENT '配置编码',
    system_id BIGINT NOT NULL COMMENT '关联SAP系统ID',
    host VARCHAR(100) NOT NULL COMMENT '主机地址',
    port INT DEFAULT 30015 COMMENT '端口',
    database_name VARCHAR(100) COMMENT '数据库名',
    tenant_name VARCHAR(100) COMMENT '租户名(MDC模式)',
    username VARCHAR(100) NOT NULL COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码(加密)',
    connection_type VARCHAR(20) DEFAULT 'JDBC' COMMENT '连接类型 JDBC/ODBC',
    pool_size INT DEFAULT 10 COMMENT '连接池大小',
    pool_min_idle INT DEFAULT 2 COMMENT '最小空闲连接',
    connection_timeout INT DEFAULT 30000 COMMENT '连接超时(ms)',
    socket_timeout INT DEFAULT 60000 COMMENT 'Socket超时(ms)',
    ssl_enabled TINYINT DEFAULT 0 COMMENT '是否启用SSL',
    other_config JSON COMMENT '其他配置',
    status TINYINT DEFAULT 1,
    created_by BIGINT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

#### 系统管理相关
```sql
-- 系统菜单配置表
CREATE TABLE system_menu (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    parent_id BIGINT DEFAULT 0 COMMENT '父菜单ID',
    menu_name VARCHAR(50) NOT NULL COMMENT '菜单名称',
    menu_code VARCHAR(50) UNIQUE NOT NULL COMMENT '菜单编码',
    menu_type VARCHAR(20) NOT NULL COMMENT '菜单类型 MENU/BUTTON/API',
    path VARCHAR(255) COMMENT '路由路径',
    component VARCHAR(255) COMMENT '组件路径',
    icon VARCHAR(50) COMMENT '图标',
    sort_order INT DEFAULT 0,
    visible TINYINT DEFAULT 1 COMMENT '是否显示 0隐藏 1显示',
    status TINYINT DEFAULT 1 COMMENT '状态',
    permission VARCHAR(100) COMMENT '权限标识',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- API端点配置表
CREATE TABLE system_api_endpoint (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    endpoint_name VARCHAR(100) NOT NULL COMMENT '端点名称',
    endpoint_code VARCHAR(50) UNIQUE NOT NULL COMMENT '端点编码',
    method VARCHAR(10) NOT NULL COMMENT '请求方法 GET/POST/PUT/DELETE',
    path VARCHAR(255) NOT NULL COMMENT '接口路径',
    module VARCHAR(50) COMMENT '所属模块',
    description VARCHAR(200),
    status TINYINT DEFAULT 1,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 验证码表
CREATE TABLE verify_code (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code_type VARCHAR(20) NOT NULL COMMENT '验证码类型 SMS/EMAIL',
    target VARCHAR(100) NOT NULL COMMENT '手机号或邮箱',
    code VARCHAR(20) NOT NULL COMMENT '验证码',
    scene VARCHAR(50) DEFAULT 'LOGIN' COMMENT '场景',
    expire_time DATETIME NOT NULL COMMENT '过期时间',
    used TINYINT DEFAULT 0 COMMENT '是否已使用',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 操作日志表
CREATE TABLE system_oper_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    module VARCHAR(50) COMMENT '模块名称',
    operation VARCHAR(50) COMMENT '操作类型',
    request_method VARCHAR(10),
    request_url VARCHAR(255),
    request_params TEXT,
    response_result TEXT,
    user_id BIGINT,
    username VARCHAR(50),
    ip_address VARCHAR(50),
    user_agent TEXT,
    execution_time BIGINT COMMENT '执行时间(ms)',
    status TINYINT DEFAULT 1,
    error_msg TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

#### 工作流相关 (预留)
```sql
-- 工作流定义表
CREATE TABLE workflow_definition (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    definition_name VARCHAR(100) NOT NULL COMMENT '流程名称',
    definition_key VARCHAR(50) UNIQUE NOT NULL COMMENT '流程标识',
    description TEXT,
    form_config JSON COMMENT '表单配置',
    flow_config JSON NOT NULL COMMENT '流程配置(状态机)',
    version INT DEFAULT 1,
    status TINYINT DEFAULT 1,
    created_by BIGINT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 工作流实例表
CREATE TABLE workflow_instance (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    definition_id BIGINT NOT NULL COMMENT '流程定义ID',
    instance_key VARCHAR(100) NOT NULL COMMENT '实例标识',
    business_key VARCHAR(100) COMMENT '业务键',
    current_state VARCHAR(50) DEFAULT 'DRAFT' COMMENT '当前状态',
    form_data JSON COMMENT '表单数据',
    applicant_id BIGINT COMMENT '申请人',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    finished_at DATETIME,
    FOREIGN KEY (definition_id) REFERENCES workflow_definition(id)
);

-- 工作流任务表
CREATE TABLE workflow_task (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    instance_id BIGINT NOT NULL COMMENT '流程实例ID',
    task_key VARCHAR(50) NOT NULL COMMENT '任务标识',
    task_name VARCHAR(100) NOT NULL COMMENT '任务名称',
    assignee_id BIGINT COMMENT '办理人',
    candidate_users JSON COMMENT '候选用户',
    candidate_groups JSON COMMENT '候选组',
    task_state VARCHAR(20) DEFAULT 'PENDING' COMMENT 'PENDING/COMPLETED/REJECTED',
    comment TEXT COMMENT '审批意见',
    variables JSON COMMENT '流程变量',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    completed_at DATETIME,
    FOREIGN KEY (instance_id) REFERENCES workflow_instance(id)
);
```

### 4.2 修改现有表

```sql
-- 修改 sys_permission 表，增加菜单权限相关字段
ALTER TABLE sys_permission ADD COLUMN menu_id BIGINT COMMENT '关联菜单ID';
ALTER TABLE sys_permission ADD COLUMN permission_type VARCHAR(20) DEFAULT 'BUTTON' COMMENT '权限类型 MENU/BUTTON/API';

-- 修改 application 表，增加系统类型字段
ALTER TABLE application ADD COLUMN system_type VARCHAR(50) COMMENT '系统类型 SAP_ABAP/SAP_JAVA/HANA/MIDDLEWARE';
ALTER TABLE application ADD COLUMN connection_config JSON COMMENT '连接配置';
```

---

## 五、非功能性需求

### 5.1 性能需求
- 页面响应时间 < 2秒
- API响应时间 < 500ms
- 报表加载时间 < 5秒

### 5.2 安全需求
- JWT Token有效期可配置
- 密码加密存储
- SQL注入防护
- XSS防护

### 5.3 可用性需求
- 支持7x24小时运行
- 故障恢复时间 < 30分钟

---

## 六、验收标准汇总

| 模块 | 验收项 | 优先级 |
|------|--------|--------|
| 布局 | 响应式布局正常 | P0 |
| 布局 | 菜单可折叠/固定 | P0 |
| 报表 | 报表创建/配置/展示 | P0 |
| 报表 | 数据源切换正常 | P0 |
| 报表 | 过滤器生效 | P0 |
| 测试工具 | API测试正常 | P1 |
| 测试工具 | SQL测试正常 | P1 |
| 权限 | 菜单权限分配正常 | P0 |
| 权限 | 按钮权限控制正常 | P0 |
| 告警 | 告警规则配置正常 | P1 |
| 告警 | 通知发送正常 | P1 |
| 应用 | 系统配置正常 | P0 |
| SAP连接 | RFC连接正常 | P1 |
| SAP连接 | API连接正常 | P1 |
| SAP连接 | WebService连接正常 | P1 |
| SAP连接 | HANA连接正常 | P1 |
| 密码 | 手机重置正常 | P1 |
| 密码 | 邮箱重置正常 | P1 |

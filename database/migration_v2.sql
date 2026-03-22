-- ============================================
-- ManagementPlat 数据库迁移脚本 V2
-- 包含: 报表/告警/SAP连接/系统管理/工作流
-- 执行时间: 2026-03-22
-- ============================================

-- -------------------------------------------
-- 一、报表相关表
-- -------------------------------------------

-- 报表配置表
CREATE TABLE IF NOT EXISTS monitor_report_config (
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
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_report_code (report_code),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='报表配置表';

-- 报表组件表
CREATE TABLE IF NOT EXISTS monitor_report_widget (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    report_id BIGINT NOT NULL COMMENT '报表ID',
    widget_name VARCHAR(100) NOT NULL COMMENT '组件名称',
    widget_type VARCHAR(50) NOT NULL COMMENT '组件类型 LINE/BAR/PIE/SCATTER/GAUGE/TABLE/RADAR/HEATMAP',
    data_source_type VARCHAR(20) NOT NULL COMMENT '数据源类型 SQL/HANA/API/WS/FILE',
    data_source_config JSON NOT NULL COMMENT '数据源配置',
    chart_config JSON COMMENT '图表配置',
    position_config JSON COMMENT '位置尺寸配置',
    sort_order INT DEFAULT 0 COMMENT '排序',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_report_id (report_id),
    FOREIGN KEY (report_id) REFERENCES monitor_report_config(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='报表组件表';

-- -------------------------------------------
-- 二、告警相关表
-- -------------------------------------------

-- 告警规则表
CREATE TABLE IF NOT EXISTS monitor_alert_rule (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    rule_name VARCHAR(100) NOT NULL COMMENT '规则名称',
    rule_code VARCHAR(50) UNIQUE NOT NULL COMMENT '规则编码',
    metric_type VARCHAR(50) NOT NULL COMMENT '指标类型',
    condition_type VARCHAR(20) NOT NULL COMMENT '条件类型 GT/LT/EQ/NE/GE/LE/BETWEEN',
    threshold_value VARCHAR(100) COMMENT '阈值',
    threshold_value_max VARCHAR(100) COMMENT '最大值(区间类型)',
    time_window INT DEFAULT 300 COMMENT '时间窗口(秒)',
    system_id BIGINT COMMENT '关联系统ID',
    status TINYINT DEFAULT 1 COMMENT '状态 0禁用 1启用',
    created_by BIGINT COMMENT '创建人',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_rule_code (rule_code),
    INDEX idx_status (status),
    INDEX idx_system_id (system_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='告警规则表';

-- 告警接收人表
CREATE TABLE IF NOT EXISTS monitor_alert_receiver (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    receiver_name VARCHAR(100) NOT NULL COMMENT '接收人名称',
    receiver_type VARCHAR(20) NOT NULL COMMENT '接收类型 USER/GROUP',
    user_id BIGINT COMMENT '用户ID',
    group_name VARCHAR(100) COMMENT '用户组名称',
    notify_channels JSON NOT NULL COMMENT '通知渠道 EMAIL/SMS/WEBHOOK',
    notify_config JSON COMMENT '通知配置',
    status TINYINT DEFAULT 1 COMMENT '状态',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_receiver_type (receiver_type),
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='告警接收人表';

-- 告警规则-接收人关联表
CREATE TABLE IF NOT EXISTS monitor_alert_rule_receiver (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    rule_id BIGINT NOT NULL COMMENT '规则ID',
    receiver_id BIGINT NOT NULL COMMENT '接收人ID',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_rule_receiver (rule_id, receiver_id),
    FOREIGN KEY (rule_id) REFERENCES monitor_alert_rule(id) ON DELETE CASCADE,
    FOREIGN KEY (receiver_id) REFERENCES monitor_alert_receiver(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='告警规则接收人关联表';

-- 告警历史表
CREATE TABLE IF NOT EXISTS monitor_alert_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    rule_id BIGINT NOT NULL COMMENT '规则ID',
    rule_name VARCHAR(100) COMMENT '规则名称',
    alert_level VARCHAR(20) DEFAULT 'WARNING' COMMENT '告警级别 WARNING/ERROR/CRITICAL',
    metric_value VARCHAR(100) COMMENT '触发时的指标值',
    alert_message TEXT COMMENT '告警消息',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态 PENDING/SENT/ACKED/CLOSED',
    sent_at DATETIME COMMENT '发送时间',
    acked_by BIGINT COMMENT '确认人',
    acked_at DATETIME COMMENT '确认时间',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_rule_id (rule_id),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at),
    FOREIGN KEY (rule_id) REFERENCES monitor_alert_rule(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='告警历史表';

-- -------------------------------------------
-- 三、SAP连接配置表
-- -------------------------------------------

-- SAP RFC连接配置表
CREATE TABLE IF NOT EXISTS sap_rfc_config (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    config_name VARCHAR(100) NOT NULL COMMENT '配置名称',
    config_code VARCHAR(50) UNIQUE NOT NULL COMMENT '配置编码',
    system_id BIGINT COMMENT '关联SAP系统ID',
    connection_type VARCHAR(20) NOT NULL COMMENT '连接类型 DIRECT/JCO',
    host VARCHAR(100) COMMENT '主机地址',
    port INT COMMENT '端口',
    client VARCHAR(10) COMMENT '客户端',
    lang VARCHAR(10) DEFAULT 'EN' COMMENT '语言',
    system_number VARCHAR(10) COMMENT '系统编号',
    pool_size INT DEFAULT 5 COMMENT '连接池大小',
    connection_config JSON COMMENT '其他连接配置',
    status TINYINT DEFAULT 1 COMMENT '状态',
    created_by BIGINT COMMENT '创建人',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_config_code (config_code),
    INDEX idx_system_id (system_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='SAP RFC连接配置表';

-- RFC函数配置表
CREATE TABLE IF NOT EXISTS sap_rfc_function (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    rfc_config_id BIGINT NOT NULL COMMENT 'RFC连接ID',
    function_name VARCHAR(100) NOT NULL COMMENT '函数名称',
    function_desc VARCHAR(200) COMMENT '函数描述',
    import_params JSON COMMENT '输入参数定义',
    export_params JSON COMMENT '输出参数定义',
    tables_params JSON COMMENT '表参数定义',
    status TINYINT DEFAULT 1 COMMENT '状态',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_rfc_function (rfc_config_id, function_name),
    INDEX idx_rfc_config_id (rfc_config_id),
    FOREIGN KEY (rfc_config_id) REFERENCES sap_rfc_config(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='RFC函数配置表';

-- SAP API接口配置表
CREATE TABLE IF NOT EXISTS sap_api_config (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    config_name VARCHAR(100) NOT NULL COMMENT '配置名称',
    config_code VARCHAR(50) UNIQUE NOT NULL COMMENT '配置编码',
    system_id BIGINT COMMENT '关联SAP系统ID',
    api_type VARCHAR(50) NOT NULL COMMENT 'API类型 OData/REST/Graph',
    base_url VARCHAR(255) NOT NULL COMMENT 'API基础地址',
    auth_type VARCHAR(20) NOT NULL COMMENT '认证类型 NONE/BASIC/OAUTH2/CERT',
    auth_config JSON COMMENT '认证配置',
    timeout INT DEFAULT 30000 COMMENT '超时时间(ms)',
    retry_count INT DEFAULT 3 COMMENT '重试次数',
    headers JSON COMMENT '自定义请求头',
    status TINYINT DEFAULT 1 COMMENT '状态',
    created_by BIGINT COMMENT '创建人',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_config_code (config_code),
    INDEX idx_system_id (system_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='SAP API接口配置表';

-- SAP WebService配置表
CREATE TABLE IF NOT EXISTS sap_webservice_config (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    config_name VARCHAR(100) NOT NULL COMMENT '配置名称',
    config_code VARCHAR(50) UNIQUE NOT NULL COMMENT '配置编码',
    system_id BIGINT COMMENT '关联SAP系统ID',
    wsdl_url VARCHAR(500) NOT NULL COMMENT 'WSDL地址',
    service_name VARCHAR(100) COMMENT '服务名称',
    port_name VARCHAR(100) COMMENT '端口名称',
    endpoint_url VARCHAR(255) COMMENT '端点地址',
    auth_type VARCHAR(20) DEFAULT 'NONE' COMMENT '认证类型 NONE/BASIC/CERT',
    auth_config JSON COMMENT '认证配置',
    status TINYINT DEFAULT 1 COMMENT '状态',
    created_by BIGINT COMMENT '创建人',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_config_code (config_code),
    INDEX idx_system_id (system_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='SAP WebService配置表';

-- HANA数据库连接配置表
CREATE TABLE IF NOT EXISTS hana_connection_config (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    config_name VARCHAR(100) NOT NULL COMMENT '配置名称',
    config_code VARCHAR(50) UNIQUE NOT NULL COMMENT '配置编码',
    system_id BIGINT COMMENT '关联SAP系统ID',
    host VARCHAR(100) NOT NULL COMMENT '主机地址',
    port INT DEFAULT 30015 COMMENT '端口',
    database_name VARCHAR(100) COMMENT '数据库名',
    tenant_name VARCHAR(100) COMMENT '租户名(MDC模式)',
    username VARCHAR(100) NOT NULL COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码(加密存储)',
    connection_type VARCHAR(20) DEFAULT 'JDBC' COMMENT '连接类型 JDBC/ODBC',
    pool_size INT DEFAULT 10 COMMENT '连接池大小',
    pool_min_idle INT DEFAULT 2 COMMENT '最小空闲连接',
    connection_timeout INT DEFAULT 30000 COMMENT '连接超时(ms)',
    socket_timeout INT DEFAULT 60000 COMMENT 'Socket超时(ms)',
    ssl_enabled TINYINT DEFAULT 0 COMMENT '是否启用SSL',
    other_config JSON COMMENT '其他配置',
    status TINYINT DEFAULT 1 COMMENT '状态',
    created_by BIGINT COMMENT '创建人',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_config_code (config_code),
    INDEX idx_system_id (system_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='HANA数据库连接配置表';

-- -------------------------------------------
-- 四、系统管理相关表
-- -------------------------------------------

-- 系统菜单配置表
CREATE TABLE IF NOT EXISTS system_menu (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    parent_id BIGINT DEFAULT 0 COMMENT '父菜单ID',
    menu_name VARCHAR(50) NOT NULL COMMENT '菜单名称',
    menu_code VARCHAR(50) UNIQUE NOT NULL COMMENT '菜单编码',
    menu_type VARCHAR(20) NOT NULL COMMENT '菜单类型 MENU/BUTTON/API',
    path VARCHAR(255) COMMENT '路由路径',
    component VARCHAR(255) COMMENT '组件路径',
    icon VARCHAR(50) COMMENT '图标',
    sort_order INT DEFAULT 0 COMMENT '排序',
    visible TINYINT DEFAULT 1 COMMENT '是否显示 0隐藏 1显示',
    status TINYINT DEFAULT 1 COMMENT '状态',
    permission VARCHAR(100) COMMENT '权限标识',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_parent_id (parent_id),
    INDEX idx_menu_code (menu_code),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统菜单配置表';

-- API端点配置表
CREATE TABLE IF NOT EXISTS system_api_endpoint (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    endpoint_name VARCHAR(100) NOT NULL COMMENT '端点名称',
    endpoint_code VARCHAR(50) UNIQUE NOT NULL COMMENT '端点编码',
    method VARCHAR(10) NOT NULL COMMENT '请求方法 GET/POST/PUT/DELETE',
    path VARCHAR(255) NOT NULL COMMENT '接口路径',
    module VARCHAR(50) COMMENT '所属模块',
    description VARCHAR(200) COMMENT '描述',
    status TINYINT DEFAULT 1 COMMENT '状态',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_endpoint_code (endpoint_code),
    INDEX idx_path (path)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='API端点配置表';

-- 验证码表
CREATE TABLE IF NOT EXISTS verify_code (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code_type VARCHAR(20) NOT NULL COMMENT '验证码类型 SMS/EMAIL',
    target VARCHAR(100) NOT NULL COMMENT '手机号或邮箱',
    code VARCHAR(20) NOT NULL COMMENT '验证码',
    scene VARCHAR(50) DEFAULT 'LOGIN' COMMENT '场景',
    expire_time DATETIME NOT NULL COMMENT '过期时间',
    used TINYINT DEFAULT 0 COMMENT '是否已使用',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_target (target),
    INDEX idx_expire_time (expire_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='验证码表';

-- 操作日志表
CREATE TABLE IF NOT EXISTS system_oper_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    module VARCHAR(50) COMMENT '模块名称',
    operation VARCHAR(50) COMMENT '操作类型',
    request_method VARCHAR(10) COMMENT '请求方法',
    request_url VARCHAR(255) COMMENT '请求URL',
    request_params TEXT COMMENT '请求参数',
    response_result TEXT COMMENT '响应结果',
    user_id BIGINT COMMENT '用户ID',
    username VARCHAR(50) COMMENT '用户名',
    ip_address VARCHAR(50) COMMENT 'IP地址',
    user_agent TEXT COMMENT 'UserAgent',
    execution_time BIGINT COMMENT '执行时间(ms)',
    status TINYINT DEFAULT 1 COMMENT '状态 0异常 1正常',
    error_msg TEXT COMMENT '错误信息',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_module (module),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';

-- -------------------------------------------
-- 五、工作流相关表 (预留)
-- -------------------------------------------

-- 工作流定义表
CREATE TABLE IF NOT EXISTS workflow_definition (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    definition_name VARCHAR(100) NOT NULL COMMENT '流程名称',
    definition_key VARCHAR(50) UNIQUE NOT NULL COMMENT '流程标识',
    description TEXT COMMENT '描述',
    form_config JSON COMMENT '表单配置',
    flow_config JSON NOT NULL COMMENT '流程配置(状态机)',
    version INT DEFAULT 1 COMMENT '版本号',
    status TINYINT DEFAULT 1 COMMENT '状态',
    created_by BIGINT COMMENT '创建人',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_definition_key (definition_key),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='工作流定义表';

-- 工作流实例表
CREATE TABLE IF NOT EXISTS workflow_instance (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    definition_id BIGINT NOT NULL COMMENT '流程定义ID',
    instance_key VARCHAR(100) NOT NULL COMMENT '实例标识',
    business_key VARCHAR(100) COMMENT '业务键',
    current_state VARCHAR(50) DEFAULT 'DRAFT' COMMENT '当前状态',
    form_data JSON COMMENT '表单数据',
    applicant_id BIGINT COMMENT '申请人',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    finished_at DATETIME COMMENT '完成时间',
    UNIQUE KEY uk_instance_key (instance_key),
    INDEX idx_definition_id (definition_id),
    INDEX idx_applicant_id (applicant_id),
    INDEX idx_current_state (current_state),
    FOREIGN KEY (definition_id) REFERENCES workflow_definition(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='工作流实例表';

-- 工作流任务表
CREATE TABLE IF NOT EXISTS workflow_task (
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
    completed_at DATETIME COMMENT '完成时间',
    INDEX idx_instance_id (instance_id),
    INDEX idx_assignee_id (assignee_id),
    INDEX idx_task_state (task_state),
    FOREIGN KEY (instance_id) REFERENCES workflow_instance(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='工作流任务表';

-- -------------------------------------------
-- 六、修改现有表
-- -------------------------------------------

-- 修改 sys_permission 表，增加菜单权限相关字段
ALTER TABLE sys_permission 
ADD COLUMN IF NOT EXISTS menu_id BIGINT COMMENT '关联菜单ID' AFTER permission_type,
ADD COLUMN IF NOT EXISTS permission_type VARCHAR(20) DEFAULT 'BUTTON' COMMENT '权限类型 MENU/BUTTON/API' AFTER description;

-- 修改 application 表，增加系统类型字段
ALTER TABLE application 
ADD COLUMN IF NOT EXISTS system_type VARCHAR(50) COMMENT '系统类型 SAP_ABAP/SAP_JAVA/HANA/MIDDLEWARE' AFTER description,
ADD COLUMN IF NOT EXISTS connection_config JSON COMMENT '连接配置' AFTER system_type;

-- -------------------------------------------
-- 七、初始数据
-- -------------------------------------------

-- 初始菜单数据
INSERT INTO system_menu (id, parent_id, menu_name, menu_code, menu_type, path, component, icon, sort_order, visible, status, permission) VALUES
-- 顶级菜单
(1, 0, '仪表盘', 'dashboard', 'MENU', '/dashboard', 'dashboard/Index', 'Odometer', 1, 1, 1, NULL),
(2, 0, '监控管理', 'monitor', 'MENU', '/monitoring', 'monitoring/Index', 'Monitor', 2, 1, 1, NULL),
(3, 0, '测试工具', 'test', 'MENU', '/test', NULL, 'Tools', 3, 1, 1, NULL),
(4, 0, '应用管理', 'application', 'MENU', '/application', 'application/Index', 'Box', 4, 1, 1, NULL),
(5, 0, '系统集成', 'integrate', 'MENU', '/integrate', 'integration/Index', 'Connection', 5, 1, 1, NULL),
(6, 0, '系统管理', 'system', 'MENU', NULL, NULL, 'Setting', 6, 1, 1, NULL),
-- 监控子菜单
(21, 2, '系统监控', 'monitor_system', 'MENU', '/monitoring/systems', 'monitoring/Systems', NULL, 1, 1, 1, 'monitor:system:view'),
(22, 2, '报表配置', 'monitor_report', 'MENU', '/monitoring/report', 'monitoring/Report', NULL, 2, 1, 1, 'monitor:report:view'),
(23, 2, '告警配置', 'monitor_alert', 'MENU', '/monitoring/alert', 'monitoring/Alert', NULL, 3, 1, 1, 'monitor:alert:view'),
-- 测试工具子菜单
(31, 3, 'API测试', 'test_api', 'MENU', '/test/api', 'test/ApiTest', NULL, 1, 1, 1, 'test:api:view'),
(32, 3, 'SQL测试', 'test_sql', 'MENU', '/test/sql', 'test/SqlTest', NULL, 2, 1, 1, 'test:sql:view'),
-- 系统管理子菜单
(61, 6, '用户管理', 'system_user', 'MENU', '/system/user', 'system/User', NULL, 1, 1, 1, 'system:user:view'),
(62, 6, '角色管理', 'system_role', 'MENU', '/system/role', 'system/Role', NULL, 2, 1, 1, 'system:role:view'),
(63, 6, '菜单管理', 'system_menu', 'MENU', '/system/menu', 'system/Menu', NULL, 3, 1, 1, 'system:menu:view'),
(64, 6, '权限管理', 'system_permission', 'MENU', '/system/permission', 'system/Permission', NULL, 4, 1, 1, 'system:permission:view'),
(65, 6, '操作日志', 'system_log', 'MENU', '/system/log', 'system/Log', NULL, 5, 1, 1, 'system:log:view');

-- 初始告警接收人
INSERT INTO monitor_alert_receiver (id, receiver_name, receiver_type, user_id, notify_channels, status) VALUES
(1, '管理员', 'USER', 1, '["EMAIL"]', 1);

-- 初始告警规则
INSERT INTO monitor_alert_rule (id, rule_name, rule_code, metric_type, condition_type, threshold_value, time_window, status) VALUES
(1, 'CPU告警', 'cpu_alert', 'SYSTEM_CPU', 'GT', '80', 300, 1),
(2, '内存告警', 'memory_alert', 'SYSTEM_MEMORY', 'GT', '85', 300, 1),
(3, 'HANA连接告警', 'hana_conn_alert', 'HANA_CONNECTION', 'EQ', '0', 60, 1);

-- 关联告警规则和接收人
INSERT INTO monitor_alert_rule_receiver (rule_id, receiver_id) VALUES
(1, 1),
(2, 1),
(3, 1);

-- 完成

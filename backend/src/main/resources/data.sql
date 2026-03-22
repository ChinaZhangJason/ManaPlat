-- =========================================
-- ManagementPlat H2 Database Initialization
-- =========================================

-- System Management Tables
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255),
    email VARCHAR(100),
    phone VARCHAR(20),
    avatar VARCHAR(255),
    status INT DEFAULT 1,
    user_type VARCHAR(20) DEFAULT 'LOCAL',
    source_id VARCHAR(100),
    last_login_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS sys_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_code VARCHAR(50) NOT NULL UNIQUE,
    role_name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    status INT DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS sys_permission (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    permission_code VARCHAR(100) NOT NULL UNIQUE,
    permission_name VARCHAR(100) NOT NULL,
    permission_type VARCHAR(20) DEFAULT 'BUTTON',
    menu_path VARCHAR(200),
    parent_id BIGINT DEFAULT 0,
    sort_order INT DEFAULT 0,
    icon VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS sys_user_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (user_id, role_id)
);

CREATE TABLE IF NOT EXISTS sys_role_permission (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (role_id, permission_id)
);

-- OAuth2 Configuration Tables
CREATE TABLE IF NOT EXISTS oauth2_provider_config (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    provider_type VARCHAR(20) NOT NULL,
    provider_name VARCHAR(100) NOT NULL,
    enabled INT DEFAULT 0,
    dingtalk_app_id VARCHAR(100),
    dingtalk_app_secret VARCHAR(255),
    oauth2_server_url VARCHAR(500),
    oauth2_client_id VARCHAR(100),
    oauth2_client_secret VARCHAR(255),
    oauth2_authorize_url VARCHAR(500),
    oauth2_token_url VARCHAR(500),
    oauth2_userinfo_url VARCHAR(500),
    oauth2_scopes VARCHAR(200) DEFAULT 'user:email',
    username_mapping VARCHAR(50) DEFAULT 'username',
    email_mapping VARCHAR(50) DEFAULT 'email',
    auto_register INT DEFAULT 0,
    default_role_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS oauth2_binding (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    provider VARCHAR(50) NOT NULL,
    open_id VARCHAR(100),
    union_id VARCHAR(100),
    access_token TEXT,
    refresh_token TEXT,
    token_expires_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- SAP System Tables
CREATE TABLE IF NOT EXISTS sap_system (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    system_id VARCHAR(50) NOT NULL UNIQUE,
    system_name VARCHAR(100) NOT NULL,
    system_type VARCHAR(50),
    description VARCHAR(500),
    connection_type VARCHAR(20) DEFAULT 'HANA_JDBC',
    hana_host VARCHAR(200),
    hana_port INT DEFAULT 30015,
    hana_database VARCHAR(100),
    hana_username VARCHAR(100),
    hana_password VARCHAR(255),
    webservice_url VARCHAR(500),
    webservice_username VARCHAR(100),
    webservice_password VARCHAR(255),
    status INT DEFAULT 1,
    sort_order INT DEFAULT 0,
    created_by BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS sap_system_action (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    system_id BIGINT NOT NULL,
    action_name VARCHAR(50) NOT NULL,
    action_code VARCHAR(50) NOT NULL,
    action_type VARCHAR(20) DEFAULT 'API',
    api_endpoint VARCHAR(500),
    sql_text TEXT,
    params TEXT,
    description VARCHAR(500),
    sort_order INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0
);

-- Monitor Tables
CREATE TABLE IF NOT EXISTS monitor_sql_config (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    config_name VARCHAR(100) NOT NULL,
    config_code VARCHAR(50) NOT NULL UNIQUE,
    category VARCHAR(50) NOT NULL,
    description VARCHAR(500),
    sql_template TEXT NOT NULL,
    param_defs TEXT,
    target_type VARCHAR(20) DEFAULT 'SINGLE',
    chart_type VARCHAR(20) DEFAULT 'TABLE',
    time_field VARCHAR(50),
    value_field VARCHAR(50),
    cache_seconds INT DEFAULT 60,
    timeout_seconds INT DEFAULT 30,
    status INT DEFAULT 1,
    created_by BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS monitor_sql_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    config_id BIGINT,
    system_id VARCHAR(50),
    sql_text TEXT,
    execute_time INT,
    result_count INT,
    status INT,
    error_msg TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS monitor_metrics_cache (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    system_id VARCHAR(50) NOT NULL,
    config_code VARCHAR(50) NOT NULL,
    cache_key VARCHAR(200) NOT NULL,
    cache_data TEXT,
    expire_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Application Table
CREATE TABLE IF NOT EXISTS application (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    app_code VARCHAR(50) NOT NULL UNIQUE,
    app_name VARCHAR(100) NOT NULL,
    app_type VARCHAR(50),
    description VARCHAR(500),
    sap_system_id BIGINT,
    form_config TEXT,
    table_config TEXT,
    status INT DEFAULT 1,
    created_by BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0
);

-- Integration Tables
CREATE TABLE IF NOT EXISTS integrate_platform (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    platform_name VARCHAR(100) NOT NULL,
    platform_code VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(500),
    base_url VARCHAR(500) NOT NULL,
    sso_mode VARCHAR(20) DEFAULT 'TOKEN',
    sso_endpoint VARCHAR(500),
    sso_secret_key VARCHAR(255),
    token_param_name VARCHAR(50) DEFAULT 'token',
    icon VARCHAR(255),
    category VARCHAR(50),
    status INT DEFAULT 1,
    sort_order INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS integrate_access_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    platform_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    access_url VARCHAR(500),
    ip_address VARCHAR(50),
    user_agent VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =========================================
-- Initial Data
-- =========================================

-- Admin user (password: admin123 - BCrypt encoded)
INSERT INTO sys_user (username, password, email, status, user_type) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', 'admin@platform.com', 1, 'LOCAL');

-- Default roles
INSERT INTO sys_role (role_code, role_name, description) VALUES
('SUPER_ADMIN', 'Super Administrator', 'Full system access'),
('ADMIN', 'Administrator', 'Admin access'),
('USER', 'Normal User', 'Normal user access');

-- Default permissions
INSERT INTO sys_permission (permission_code, permission_name, permission_type, menu_path, sort_order) VALUES
('DASHBOARD', 'Dashboard', 'MENU', '/dashboard', 1),
('MONITOR', 'Monitor', 'MENU', '/monitoring', 10),
('MONITOR_VIEW', 'Monitor View', 'MENU', '/monitoring', 11),
('MONITOR_SQL', 'SQL Config', 'MENU', '/monitoring/sql-config', 12),
('APPLICATION', 'Application', 'MENU', '/application', 20),
('APPLICATION_VIEW', 'Application View', 'BUTTON', '/application', 21),
('APPLICATION_EDIT', 'Application Edit', 'BUTTON', '/application', 22),
('INTEGRATE', 'Integration', 'MENU', '/integration', 30),
('INTEGRATE_VIEW', 'Integration View', 'MENU', '/integration', 31),
('SYSTEM', 'System', 'MENU', '/system', 90),
('USER_MANAGE', 'User Management', 'MENU', '/system/user', 91),
('ROLE_MANAGE', 'Role Management', 'MENU', '/system/role', 92);

-- Assign admin role to admin user
INSERT INTO sys_user_role (user_id, role_id) VALUES (1, 1);

-- Assign all permissions to SUPER_ADMIN role
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 1, id FROM sys_permission;

-- Assign basic permissions to USER role
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 3, id FROM sys_permission WHERE permission_type = 'MENU' AND permission_code = 'DASHBOARD';

-- OAuth2 provider configs (disabled by default)
INSERT INTO oauth2_provider_config (provider_type, provider_name, enabled) VALUES
('DINGTALK', '钉钉登录', 0),
('OAUTH2', '企业SSO', 0);

-- Sample monitor SQL configs
INSERT INTO monitor_sql_config (config_name, config_code, category, description, sql_template, chart_type) VALUES
('CPU使用率', 'CPU_USAGE', 'PERFORMANCE', 'SAP系统CPU使用率监控', 
 'SELECT HOST, SNAPSHOT_TIME, CPU FROM SYS.M_CPU_UTILIZATION WHERE SYSTEM_ID = ''{system_id}'' AND SNAPSHOT_TIME BETWEEN ''{start_time}'' AND ''{end_time}'' ORDER BY SNAPSHOT_TIME',
 'LINE'),

('内存使用率', 'MEMORY_USAGE', 'PERFORMANCE', 'SAP系统内存使用率监控',
 'SELECT HOST, SNAPSHOT_TIME, MEMORY_USED/MEMORY*100 as MEMORY_PCT FROM SYS.M_MEMORY WHERE SYSTEM_ID = ''{system_id}'' AND SNAPSHOT_TIME BETWEEN ''{start_time}'' AND ''{end_time}'' ORDER BY SNAPSHOT_TIME',
 'LINE');

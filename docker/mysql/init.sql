-- =========================================
-- ManagementPlat Database Initialization
-- =========================================

CREATE DATABASE IF NOT EXISTS manageplat DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE manageplat;

-- =========================================
-- 1. System Management - User/Role/Permission
-- =========================================

CREATE TABLE sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE COMMENT 'Username',
    password VARCHAR(255) COMMENT 'Encrypted password',
    email VARCHAR(100) COMMENT 'Email',
    phone VARCHAR(20) COMMENT 'Phone number',
    avatar VARCHAR(255) COMMENT 'Avatar URL',
    status TINYINT DEFAULT 1 COMMENT 'Status: 1=Active, 0=Disabled',
    user_type ENUM('LOCAL', 'DINGTALK', 'OAUTH2') DEFAULT 'LOCAL' COMMENT 'User type',
    source_id VARCHAR(100) COMMENT 'Third-party user ID',
    last_login_at DATETIME COMMENT 'Last login time',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0,
    INDEX idx_username (username),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='User table';

CREATE TABLE sys_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_code VARCHAR(50) NOT NULL UNIQUE COMMENT 'Role code',
    role_name VARCHAR(100) NOT NULL COMMENT 'Role name',
    description VARCHAR(255) COMMENT 'Description',
    status TINYINT DEFAULT 1 COMMENT 'Status: 1=Active, 0=Disabled',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0,
    INDEX idx_role_code (role_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Role table';

CREATE TABLE sys_permission (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    permission_code VARCHAR(100) NOT NULL UNIQUE COMMENT 'Permission code',
    permission_name VARCHAR(100) NOT NULL COMMENT 'Permission name',
    permission_type ENUM('MENU', 'BUTTON', 'API') DEFAULT 'BUTTON' COMMENT 'Permission type',
    menu_path VARCHAR(200) COMMENT 'Menu path',
    parent_id BIGINT DEFAULT 0 COMMENT 'Parent permission ID',
    sort_order INT DEFAULT 0 COMMENT 'Sort order',
    icon VARCHAR(50) COMMENT 'Icon',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0,
    INDEX idx_permission_code (permission_code),
    INDEX idx_parent_id (parent_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Permission table';

CREATE TABLE sys_user_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT 'User ID',
    role_id BIGINT NOT NULL COMMENT 'Role ID',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_role (user_id, role_id),
    INDEX idx_user_id (user_id),
    INDEX idx_role_id (role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='User-Role relation';

CREATE TABLE sys_role_permission (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_id BIGINT NOT NULL COMMENT 'Role ID',
    permission_id BIGINT NOT NULL COMMENT 'Permission ID',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_role_permission (role_id, permission_id),
    INDEX idx_role_id (role_id),
    INDEX idx_permission_id (permission_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Role-Permission relation';

-- =========================================
-- 2. OAuth2 Configuration
-- =========================================

CREATE TABLE oauth2_provider_config (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    provider_type ENUM('DINGTALK', 'OAUTH2', 'CAS') NOT NULL COMMENT 'Provider type',
    provider_name VARCHAR(100) NOT NULL COMMENT 'Provider display name',
    enabled TINYINT DEFAULT 0 COMMENT 'Enabled: 1=Yes, 0=No',
    
    dingtalk_app_id VARCHAR(100) COMMENT 'DingTalk App ID',
    dingtalk_app_secret VARCHAR(255) COMMENT 'DingTalk App Secret',
    
    oauth2_server_url VARCHAR(500) COMMENT 'OAuth2 Server URL',
    oauth2_client_id VARCHAR(100) COMMENT 'OAuth2 Client ID',
    oauth2_client_secret VARCHAR(255) COMMENT 'OAuth2 Client Secret',
    oauth2_authorize_url VARCHAR(500) COMMENT 'OAuth2 Authorize URL',
    oauth2_token_url VARCHAR(500) COMMENT 'OAuth2 Token URL',
    oauth2_userinfo_url VARCHAR(500) COMMENT 'OAuth2 UserInfo URL',
    oauth2_scopes VARCHAR(200) DEFAULT 'user:email' COMMENT 'OAuth2 Scopes',
    
    username_mapping VARCHAR(50) DEFAULT 'username' COMMENT 'Username field mapping',
    email_mapping VARCHAR(50) DEFAULT 'email' COMMENT 'Email field mapping',
    
    auto_register TINYINT DEFAULT 0 COMMENT 'Auto register: 1=Yes, 0=No',
    default_role_id BIGINT COMMENT 'Default role for auto-registered users',
    
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0,
    INDEX idx_provider_type (provider_type),
    INDEX idx_enabled (enabled)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='OAuth2 provider configuration';

CREATE TABLE oauth2_binding (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT 'Local user ID',
    provider VARCHAR(50) NOT NULL COMMENT 'Provider: dingtalk/oauth2',
    open_id VARCHAR(100) COMMENT 'Open ID from provider',
    union_id VARCHAR(100) COMMENT 'Union ID from provider',
    access_token TEXT COMMENT 'Access token',
    refresh_token TEXT COMMENT 'Refresh token',
    token_expires_at DATETIME COMMENT 'Token expiration time',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_provider (user_id, provider),
    INDEX idx_open_id (open_id),
    INDEX idx_union_id (union_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='OAuth2 user binding';

-- =========================================
-- 3. SAP System Configuration
-- =========================================

CREATE TABLE sap_system (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    system_id VARCHAR(50) NOT NULL UNIQUE COMMENT 'System ID (e.g., S4H_001)',
    system_name VARCHAR(100) NOT NULL COMMENT 'System display name',
    system_type VARCHAR(50) COMMENT 'System type: S4/HANA/ECC/BW',
    description VARCHAR(500) COMMENT 'Description',
    
    connection_type ENUM('HANA', 'HANA_JDBC', 'WEBSERVICE') DEFAULT 'HANA_JDBC' COMMENT 'Connection type',
    
    hana_host VARCHAR(200) COMMENT 'HANA host',
    hana_port INT DEFAULT 30015 COMMENT 'HANA port',
    hana_database VARCHAR(100) COMMENT 'HANA database name',
    hana_username VARCHAR(100) COMMENT 'HANA username',
    hana_password VARCHAR(255) COMMENT 'HANA password (encrypted)',
    
    webservice_url VARCHAR(500) COMMENT 'WebService URL',
    webservice_username VARCHAR(100) COMMENT 'WebService username',
    webservice_password VARCHAR(255) COMMENT 'WebService password',
    
    status TINYINT DEFAULT 1 COMMENT 'Status: 1=Active, 0=Inactive',
    sort_order INT DEFAULT 0 COMMENT 'Sort order',
    created_by BIGINT COMMENT 'Creator ID',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0,
    INDEX idx_system_id (system_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='SAP system configuration';

CREATE TABLE sap_system_action (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    system_id BIGINT NOT NULL COMMENT 'SAP system ID',
    action_name VARCHAR(50) NOT NULL COMMENT 'Action name',
    action_code VARCHAR(50) NOT NULL COMMENT 'Action code',
    action_type ENUM('API', 'PROCEDURE', 'SQL') DEFAULT 'API' COMMENT 'Action type',
    api_endpoint VARCHAR(500) COMMENT 'API endpoint',
    sql_text TEXT COMMENT 'SQL or procedure',
    params JSON COMMENT 'Parameters definition',
    description VARCHAR(500) COMMENT 'Description',
    sort_order INT DEFAULT 0 COMMENT 'Sort order',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0,
    INDEX idx_system_id (system_id),
    INDEX idx_action_code (action_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='SAP system custom actions';

-- =========================================
-- 4. Monitor SQL Configuration
-- =========================================

CREATE TABLE monitor_sql_config (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    config_name VARCHAR(100) NOT NULL COMMENT 'Config name',
    config_code VARCHAR(50) NOT NULL UNIQUE COMMENT 'Config code',
    category VARCHAR(50) NOT NULL COMMENT 'Category: PERFORMANCE/LOGS/JOBS',
    description VARCHAR(500) COMMENT 'Description',
    
    sql_template TEXT NOT NULL COMMENT 'SQL template with placeholders',
    param_defs JSON COMMENT 'Parameter definitions: [{name, type, required, defaultValue}]',
    
    target_type ENUM('SINGLE', 'MULTI') DEFAULT 'SINGLE' COMMENT 'Target system: single or multiple',
    chart_type ENUM('LINE', 'BAR', 'PIE', 'TABLE', 'GAUGE') DEFAULT 'TABLE' COMMENT 'Chart type',
    time_field VARCHAR(50) COMMENT 'Time field for chart X-axis',
    value_field VARCHAR(50) COMMENT 'Value field for chart Y-axis',
    
    cache_seconds INT DEFAULT 60 COMMENT 'Cache duration in seconds',
    timeout_seconds INT DEFAULT 30 COMMENT 'Query timeout in seconds',
    
    status TINYINT DEFAULT 1 COMMENT 'Status: 1=Active, 0=Inactive',
    created_by BIGINT COMMENT 'Creator ID',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0,
    INDEX idx_config_code (config_code),
    INDEX idx_category (category),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Monitor SQL configuration';

CREATE TABLE monitor_sql_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    config_id BIGINT COMMENT 'SQL config ID',
    system_id VARCHAR(50) COMMENT 'SAP system ID',
    sql_text TEXT COMMENT 'Executed SQL',
    execute_time INT COMMENT 'Execution time (ms)',
    result_count INT COMMENT 'Result count',
    status TINYINT COMMENT 'Status: 1=Success, 0=Failed',
    error_msg TEXT COMMENT 'Error message if failed',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_config_id (config_id),
    INDEX idx_system_id (system_id),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Monitor SQL execution log';

CREATE TABLE monitor_metrics_cache (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    system_id VARCHAR(50) NOT NULL COMMENT 'SAP system ID',
    config_code VARCHAR(50) NOT NULL COMMENT 'Config code',
    cache_key VARCHAR(200) NOT NULL COMMENT 'Cache key',
    cache_data LONGTEXT COMMENT 'Cached data (JSON)',
    expire_at DATETIME NOT NULL COMMENT 'Expiration time',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_cache_key (cache_key),
    INDEX idx_expire_at (expire_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Monitor metrics cache';

-- =========================================
-- 5. Application Management
-- =========================================

CREATE TABLE application (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    app_code VARCHAR(50) NOT NULL UNIQUE COMMENT 'Application code',
    app_name VARCHAR(100) NOT NULL COMMENT 'Application name',
    app_type VARCHAR(50) COMMENT 'Application type',
    description VARCHAR(500) COMMENT 'Description',
    
    sap_system_id BIGINT COMMENT 'Linked SAP system',
    
    form_config JSON COMMENT 'Form configuration (fields, validation)',
    table_config JSON COMMENT 'Table/list configuration',
    
    status TINYINT DEFAULT 1 COMMENT 'Status: 1=Active, 0=Inactive',
    created_by BIGINT COMMENT 'Creator ID',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0,
    INDEX idx_app_code (app_code),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Application configuration';

-- =========================================
-- 6. Third-party Integration
-- =========================================

CREATE TABLE integrate_platform (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    platform_name VARCHAR(100) NOT NULL COMMENT 'Platform name',
    platform_code VARCHAR(50) NOT NULL UNIQUE COMMENT 'Platform code',
    description VARCHAR(500) COMMENT 'Description',
    
    base_url VARCHAR(500) NOT NULL COMMENT 'Platform base URL',
    
    sso_mode ENUM('TOKEN', 'COOKIE', 'NONE') DEFAULT 'TOKEN' COMMENT 'SSO mode',
    sso_endpoint VARCHAR(500) COMMENT 'SSO token endpoint',
    sso_secret_key VARCHAR(255) COMMENT 'SSO secret for token generation',
    token_param_name VARCHAR(50) DEFAULT 'token' COMMENT 'Token parameter name',
    
    icon VARCHAR(255) COMMENT 'Platform icon URL',
    category VARCHAR(50) COMMENT 'Category: MONITOR/LOG/JIRA/OTHER',
    
    status TINYINT DEFAULT 1 COMMENT 'Status: 1=Active, 0=Inactive',
    sort_order INT DEFAULT 0 COMMENT 'Sort order',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0,
    INDEX idx_platform_code (platform_code),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Third-party platform configuration';

CREATE TABLE integrate_access_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    platform_id BIGINT NOT NULL COMMENT 'Platform ID',
    user_id BIGINT NOT NULL COMMENT 'User ID',
    access_url VARCHAR(500) COMMENT 'Access URL',
    ip_address VARCHAR(50) COMMENT 'IP address',
    user_agent VARCHAR(500) COMMENT 'User agent',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_platform_id (platform_id),
    INDEX idx_user_id (user_id),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Integration access log';

-- =========================================
-- 7. Initial Data
-- =========================================

-- Insert default admin user (password: admin123)
INSERT INTO sys_user (username, password, email, status, user_type) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', 'admin@platform.com', 1, 'LOCAL');

-- Insert default roles
INSERT INTO sys_role (role_code, role_name, description) VALUES
('SUPER_ADMIN', 'Super Administrator', 'Full system access'),
('ADMIN', 'Administrator', 'Admin access'),
('USER', 'Normal User', 'Normal user access');

-- Insert default permissions
INSERT INTO sys_permission (permission_code, permission_name, permission_type, menu_path, sort_order) VALUES
('DASHBOARD', 'Dashboard', 'MENU', '/dashboard', 1),
('MONITOR', 'Monitor', 'MENU', '/monitor', 10),
('MONITOR_VIEW', 'Monitor View', 'MENU', '/monitor/dashboard', 11),
('MONITOR_SQL', 'SQL Config', 'MENU', '/monitor/sql-config', 12),
('APPLICATION', 'Application', 'MENU', '/application', 20),
('APPLICATION_VIEW', 'Application View', 'BUTTON', '/application', 21),
('APPLICATION_EDIT', 'Application Edit', 'BUTTON', '/application', 22),
('INTEGRATE', 'Integration', 'MENU', '/integrate', 30),
('INTEGRATE_VIEW', 'Integration View', 'MENU', '/integrate/platforms', 31),
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
SELECT 3, id FROM sys_permission WHERE permission_type = 'MENU' AND permission_code IN ('DASHBOARD');

-- Insert default OAuth2 provider config (disabled by default)
INSERT INTO oauth2_provider_config (provider_type, provider_name, enabled, oauth2_server_url, oauth2_client_id) VALUES
('DINGTALK', '钉钉登录', 0, '', ''),
('OAUTH2', '企业SSO', 0, '', '');

-- Insert default monitor SQL configs
INSERT INTO monitor_sql_config (config_name, config_code, category, description, sql_template, param_defs, chart_type) VALUES
('CPU使用率', 'CPU_USAGE', 'PERFORMANCE', 'SAP系统CPU使用率监控', 
 'SELECT HOST, SNAPSHOT_TIME, CPU FROM SYS.M_CPU_UTILIZATION WHERE SYSTEM_ID = ''{system_id}'' AND SNAPSHOT_TIME BETWEEN ''{start_time}'' AND ''{end_time}'' ORDER BY SNAPSHOT_TIME',
 '[{"name":"system_id","type":"STRING","required":true},{"name":"start_time","type":"DATETIME","required":true},{"name":"end_time","type":"DATETIME","required":true}]',
 'LINE'),

('内存使用率', 'MEMORY_USAGE', 'PERFORMANCE', 'SAP系统内存使用率监控',
 'SELECT HOST, SNAPSHOT_TIME, MEMORY_USED/MEMORY*100 as MEMORY_PCT FROM SYS.M_MEMORY WHERE SYSTEM_ID = ''{system_id}'' AND SNAPSHOT_TIME BETWEEN ''{start_time}'' AND ''{end_time}'' ORDER BY SNAPSHOT_TIME',
 '[{"name":"system_id","type":"STRING","required":true},{"name":"start_time","type":"DATETIME","required":true},{"name":"end_time","type":"DATETIME","required":true}]',
 'LINE');

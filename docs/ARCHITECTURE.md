# 架构设计文档

## 一、设计目标

1. **模块化设计** - 各功能模块独立，便于维护和扩展
2. **配置化管理** - 所有第三方集成通过数据库配置，支持动态管理
3. **安全认证** - 支持本地认证和OAuth2.0第三方认证
4. **可扩展性** - SQL配置化，支持自定义监控指标

## 二、认证体系设计

### 2.1 混合认证架构

```
┌──────────────────────────────────────────────────────────────────┐
│                    登录页面 (Vue3)                                │
│   ┌─────────────┐  ┌─────────────┐  ┌─────────────┐              │
│   │ 用户名/密码  │  │   钉钉扫码   │  │  OAuth2登录  │              │
│   └──────┬──────┘  └──────┬──────┘  └──────┬──────┘              │
└──────────┼────────────────┼────────────────┼─────────────────────┘
           │                │                │
           ▼                ▼                ▼
┌──────────────────────────────────────────────────────────────────┐
│                  Spring Security 认证层                           │
│  ┌────────────────┐  ┌────────────────┐  ┌────────────────┐       │
│  │  LocalAuth     │  │  DingTalkAuth  │  │  OAuth2Auth     │       │
│  │  (本地用户)    │  │  (钉钉SSO)     │  │  (通用OAuth2)   │       │
│  └───────┬────────┘  └───────┬────────┘  └───────┬────────┘       │
│          └──────────────────┬─────────────────────┘                │
│                             ▼                                      │
│                    ┌────────────────┐                              │
│                    │   User Service  │                              │
│                    │   (统一用户)    │                              │
│                    └────────┬────────┘                              │
│                             ▼                                      │
│                    ┌────────────────┐                              │
│                    │   JWT Token    │                              │
│                    │  (会话管理)    │                              │
│                    └────────────────┘                              │
└──────────────────────────────────────────────────────────────────┘
```

### 2.2 JWT Token结构

```json
{
  "sub": "user_id",
  "username": "admin",
  "roles": ["SUPER_ADMIN"],
  "permissions": ["USER_MANAGE", "MONITOR_VIEW"],
  "iat": 1699999999,
  "exp": 1700086399
}
```

### 2.3 OAuth2.0集成流程

```
1. 用户点击第三方登录
2. 前端跳转到 /api/auth/oauth2/{provider}
3. 后端生成state，跳转到提供商授权页面
4. 用户授权后回调 /api/auth/oauth2/{provider}/callback?code=xxx&state=xxx
5. 后端用code换取access_token
6. 调用用户信息接口获取用户信息
7. 绑定或创建本地用户
8. 返回JWT Token
```

## 三、SAP监控模块设计

### 3.1 动态SQL配置

```sql
-- SQL模板，支持参数占位符
SELECT HOST, CPU, SNAPSHOT_TIME
FROM SYS.M_CPU_UTILIZATION
WHERE SYSTEM_ID = '{system_id}'
  AND SNAPSHOT_TIME BETWEEN '{start_time}' AND '{end_time}'
```

**参数定义 (JSON)**
```json
[
  {"name": "system_id", "type": "STRING", "required": true},
  {"name": "start_time", "type": "DATETIME", "required": true},
  {"name": "end_time", "type": "DATETIME", "required": true}
]
```

### 3.2 HANA多数据源

```java
// 动态数据源配置
@Configuration
public class HanaDataSourceConfig {
    
    @Bean
    public Map<String, DataSource> hanaDataSources() {
        // 从数据库读取HANA配置
        List<HanaConfig> configs = hanaConfigService.getActiveConfigs();
        
        Map<String, DataSource> dataSources = new HashMap<>();
        for (HanaConfig config : configs) {
            DataSource ds = createDataSource(config);
            dataSources.put(config.getSystemId(), ds);
        }
        return dataSources;
    }
}
```

### 3.3 缓存策略

| 缓存级别 | TTL | 说明 |
|----------|-----|------|
| 实时 | 0 | 每次请求都查询 |
| 短期 | 60s | 常规监控指标 |
| 中期 | 300s | 统计类数据 |
| 长期 | 3600s | 历史数据汇总 |

## 四、第三方集成设计

### 4.1 SSO Token生成

```java
// 生成第三方访问Token
@Service
public class SsoTokenService {
    
    public String generateToken(Long userId, String platformCode) {
        // 1. 验证用户权限
        // 2. 生成短期Token (5分钟有效期)
        String token = JwtTokenProvider.builder()
            .userId(userId)
            .platform(platformCode)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + 300000))
            .signWith(secretKey);
        return token;
    }
}
```

### 4.2 iframe嵌入流程

```
1. 用户选择第三方平台
2. 前端请求 /api/integrate/sso-token?platform=xxx
3. 后端验证权限，生成Token
4. 前端组装URL: https://third-party.com/?token=xxx
5. iframe加载目标页面
6. 第三方平台解析Token完成SSO
```

## 五、权限模型

### 5.1 RBAC三层模型

```
用户 ───属于───> 角色 ───包含───> 权限
   (多对多)        (多对多)
   
权限类型:
- MENU: 菜单权限
- BUTTON: 按钮权限  
- API: 接口权限
```

### 5.2 权限控制注解

```java
@RequiresPermissions("USER_MANAGE")
public Result<?> updateUser(UserDTO dto) { }

@RequiresPermissions(value = {"USER_MANAGE", "ROLE_MANAGE"}, logical = Logical.OR)
public Result<?> batchUpdate() { }
```

## 六、数据模型

### 6.1 统一响应结构

```json
{
  "code": 200,
  "message": "success",
  "data": { },
  "timestamp": 1699999999
}
```

### 6.2 分页响应

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [],
    "total": 100,
    "size": 10,
    "current": 1,
    "pages": 10
  },
  "timestamp": 1699999999
}
```

## 七、安全设计

### 7.1 密码加密

- 算法: BCrypt
- 强度: 10 rounds
- 盐值: 自动生成

### 7.2 SQL注入防护

- 参数化查询 (MyBatis)
- 禁止动态SQL拼接用户输入
- SQL执行日志记录

### 7.3 XSS防护

- 输入过滤
- 输出编码
- Content-Type设置

### 7.4 CSRF防护

- Token验证
- SameSite Cookie

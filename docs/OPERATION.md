# 运维手册

## 一、日常运维

### 1.1 服务状态检查

```bash
# 检查所有服务状态
docker-compose ps

# 检查服务健康状态
curl http://localhost:8080/api/health
curl http://localhost/api/health

# 检查端口监听
netstat -tlnp | grep -E '80|8080|3306'
```

### 1.2 日志监控

```bash
# 实时查看后端日志
docker-compose logs -f backend

# 查看错误日志
docker-compose logs backend 2>&1 | grep -i error

# 查看最近1小时的日志
docker-compose logs --since=60m backend

# 日志级别配置
# 在 application.yml 中
logging:
  level:
    root: INFO
    com.platform: DEBUG
```

### 1.3 性能监控

```bash
# 查看容器资源使用
docker stats

# 查看MySQL连接数
docker exec manageplat-mysql mysql -u root -p${DB_PASSWORD} -e "SHOW STATUS LIKE 'Threads_connected';"

# 查看慢查询
docker exec manageplat-mysql mysql -u root -p${DB_PASSWORD} -e "SHOW GLOBAL STATUS LIKE 'Slow_queries';"
```

## 二、数据库运维

### 2.1 日常备份

```bash
# 自动备份脚本 (建议每天执行)
#!/bin/bash
DATE=$(date +%Y%m%d_%H%M%S)
BACKUP_DIR=/backup/manageplat
mkdir -p $BACKUP_DIR

docker exec manageplat-mysql mysqldump -u root -p${DB_PASSWORD} \
  --single-transaction --routines --triggers manageplat \
  | gzip > $BACKUP_DIR/manageplat_$DATE.sql.gz

# 保留最近30天备份
find $BACKUP_DIR -name "*.sql.gz" -mtime +30 -delete

echo "Backup completed: manageplat_$DATE.sql.gz"
```

### 2.2 数据恢复

```bash
# 停止服务
docker-compose stop backend

# 恢复数据
gunzip < backup_20240101_120000.sql.gz | docker exec -i manageplat-mysql mysql -u root -p${DB_PASSWORD} manageplat

# 启动服务
docker-compose start backend
```

### 2.3 常用SQL

```sql
-- 查看表大小
SELECT table_name, round(data_length / 1024 / 1024, 2) as 'MB'
FROM information_schema.tables 
WHERE table_schema = 'manageplat'
ORDER BY data_length DESC;

-- 查看慢查询
SHOW FULL PROCESSLIST;

-- 查看连接数
SHOW STATUS LIKE 'Threads_connected';
SHOW VARIABLES LIKE 'max_connections';

-- 清理告警历史 (保留30天)
DELETE FROM monitor_alert_history WHERE created_at < DATE_SUB(NOW(), INTERVAL 30 DAY);

-- 清理操作日志 (保留90天)
DELETE FROM system_oper_log WHERE created_at < DATE_SUB(NOW(), INTERVAL 90 DAY);
```

## 三、应用运维

### 3.1 重启服务

```bash
# 重启后端
docker-compose restart backend

# 重启所有服务
docker-compose restart

# 重启并清除缓存
docker-compose restart backend
docker exec manageplat-backend rm -rf /tmp/spring-boot-loader/
```

### 3.2 JVM调优

```yaml
# docker-compose.yml
environment:
  JAVA_OPTS: >-
    -Xms1g
    -Xmx2g
    -XX:+UseG1GC
    -XX:MaxGCPauseMillis=200
    -XX:+HeapDumpOnOutOfMemoryError
    -XX:HeapDumpPath=/var/log/heapdump.hprof
```

### 3.3 缓存管理

```bash
# 清理Redis缓存
docker exec manageplat-redis redis-cli FLUSHALL

# 查看缓存Key
docker exec manageplat-redis redis-cli KEYS "*"

# 查看缓存大小
docker exec manageplat-redis redis-cli DBSIZE
```

## 四、告警配置

### 4.1 告警规则

```sql
-- CPU使用率告警
INSERT INTO monitor_alert_rule (rule_name, rule_code, metric_type, condition_type, threshold_value, time_window)
VALUES ('CPU告警', 'cpu_alert', 'SYSTEM_CPU', 'GT', '80', 300);

-- 内存使用率告警
INSERT INTO monitor_alert_rule (rule_name, rule_code, metric_type, condition_type, threshold_value, time_window)
VALUES ('内存告警', 'memory_alert', 'SYSTEM_MEMORY', 'GT', '85', 300);

-- HANA连接失败告警
INSERT INTO monitor_alert_rule (rule_name, rule_code, metric_type, condition_type, threshold_value, time_window)
VALUES ('HANA连接告警', 'hana_conn_alert', 'HANA_CONNECTION', 'EQ', '0', 60);
```

### 4.2 告警接收人

```sql
-- 添加接收人
INSERT INTO monitor_alert_receiver (receiver_name, receiver_type, user_id, notify_channels)
VALUES ('管理员', 'USER', 1, '["EMAIL", "SMS"]');

-- 关联规则和接收人
INSERT INTO monitor_alert_rule_receiver (rule_id, receiver_id) VALUES (1, 1);
```

### 4.3 告警阈值参考

| 指标 | 警告阈值 | 严重阈值 | 紧急阈值 |
|------|----------|----------|----------|
| CPU使用率 | 70% | 85% | 95% |
| 内存使用率 | 75% | 85% | 95% |
| 磁盘使用率 | 80% | 90% | 95% |
| 数据库连接 | 70% | 85% | 95% |
| API响应时间 | 2s | 5s | 10s |
| HANA查询时间 | 5s | 10s | 30s |

## 五、安全运维

### 5.1 密码策略

```sql
-- 设置密码复杂度要求 (应用层实现)
-- 最小长度8位
-- 必须包含大小写字母
-- 必须包含数字
-- 必须包含特殊字符

-- 密码过期策略 (90天)
ALTER TABLE sys_user MODIFY COLUMN password_expire_date DATE;
```

### 5.2 日志审计

```sql
-- 查看登录日志
SELECT * FROM system_oper_log WHERE module = 'AUTH' ORDER BY created_at DESC LIMIT 100;

-- 查看异常登录
SELECT * FROM system_oper_log 
WHERE status = 0 AND module = 'AUTH' 
ORDER BY created_at DESC LIMIT 50;

-- 查看敏感操作
SELECT * FROM system_oper_log 
WHERE operation IN ('DELETE', 'UPDATE') 
AND request_url LIKE '%user%'
ORDER BY created_at DESC LIMIT 100;
```

### 5.3 权限审计

```sql
-- 查看用户角色
SELECT u.username, r.role_name 
FROM sys_user u
JOIN sys_user_role ur ON u.id = ur.user_id
JOIN sys_role r ON ur.role_id = r.id;

-- 查看角色权限
SELECT r.role_name, p.permission_name, p.permission_code
FROM sys_role r
JOIN sys_role_permission rp ON r.id = rp.role_id
JOIN sys_permission p ON rp.permission_id = p.id;
```

## 六、监控配置

### 6.1 系统监控指标

| 指标 | 采集间隔 | 存储时长 |
|------|----------|----------|
| CPU使用率 | 60s | 30天 |
| 内存使用率 | 60s | 30天 |
| 磁盘使用率 | 300s | 30天 |
| 网络流量 | 60s | 30天 |
| JVM内存 | 60s | 30天 |
| 数据库连接 | 60s | 30天 |

### 6.2 报表缓存策略

```sql
-- 查看缓存配置
SELECT * FROM monitor_sql_config WHERE cache_seconds > 0;

-- 清理报表缓存
DELETE FROM monitor_metrics_cache WHERE created_at < DATE_SUB(NOW(), INTERVAL 1 DAY);

-- 手动刷新缓存
-- 通过API调用
POST /api/monitor/cache/refresh?configId=1
```

## 七、故障处理

### 7.1 服务不可用

```bash
# 1. 检查服务状态
docker-compose ps

# 2. 检查日志
docker-compose logs --tail=200 backend

# 3. 重启服务
docker-compose restart backend

# 4. 如仍有问题，检查端口占用
netstat -tlnp | grep 8080

# 5. 检查防火墙
systemctl status firewalld
iptables -L -n
```

### 7.2 数据库连接失败

```bash
# 1. 检查MySQL服务
docker-compose ps mysql

# 2. 检查MySQL日志
docker-compose logs mysql

# 3. 测试连接
docker exec manageplat-backend ping mysql

# 4. 检查连接数
docker exec manageplat-mysql mysql -u root -p${DB_PASSWORD} -e "SHOW STATUS LIKE 'Threads_connected';"

# 5. 重启MySQL
docker-compose restart mysql
```

### 7.3 前端白屏

```bash
# 1. 清除浏览器缓存
# Ctrl+Shift+R (强制刷新)

# 2. 检查Nginx
docker exec manageplat-nginx cat /etc/nginx/conf.d/default.conf

# 3. 重启Nginx
docker-compose restart nginx

# 4. 检查前端构建
docker exec manageplat-frontend ls -la /usr/share/nginx/html
```

### 7.4 性能下降

```bash
# 1. 查看资源使用
docker stats

# 2. 查看慢查询
docker exec manageplat-mysql mysql -u root -p${DB_PASSWORD} -e "SHOW FULL PROCESSLIST;"

# 3. 查看JVM
docker exec manageplat-backend jstat -gc $(jps | grep jar | awk '{print $1}')

# 4. 优化建议
# - 增加连接池大小
# - 添加索引
# - 清理历史数据
# - 重启服务释放内存
```

## 八、巡检清单

### 每日巡检

- [ ] 服务状态正常
- [ ] 磁盘空间充足 (>20%)
- [ ] 内存使用正常
- [ ] 告警列表为空或已处理
- [ ] 备份任务执行成功

### 每周巡检

- [ ] 数据库大小增长趋势
- [ ] 日志文件大小
- [ ] 慢查询数量
- [ ] 用户活跃度
- [ ] 安全日志

### 每月巡检

- [ ] 数据库性能分析
- [ ] 索引优化
- [ ] 备份恢复测试
- [ ] 证书有效期
- [ ] 安全漏洞扫描

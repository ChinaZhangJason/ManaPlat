# 部署文档

## 一、环境要求

### 硬件要求

| 配置 | 开发环境 | 测试环境 | 生产环境 |
|------|----------|----------|----------|
| CPU | 2核+ | 4核+ | 8核+ |
| 内存 | 4GB+ | 8GB+ | 16GB+ |
| 磁盘 | 50GB+ | 100GB+ | 200GB+ |

### 软件要求

| 软件 | 版本 | 说明 |
|------|------|------|
| JDK | 17+ | 后端运行环境 |
| Node.js | 18+ | 前端构建环境 |
| MySQL | 8.0+ | 主数据库 |
| Redis | 6.0+ | 缓存(可选) |
| Docker | 20.10+ | 容器化部署 |

## 二、快速部署

### 2.1 下载代码

```bash
git clone <repository_url>
cd ManagementPlat
```

### 2.2 配置环境变量

```bash
cd docker
cp .env.example .env
# 编辑 .env 文件配置数据库密码等
```

### 2.3 环境变量说明

```bash
# 数据库配置
DB_HOST=mysql
DB_PORT=3306
DB_NAME=manageplat
DB_USERNAME=root
DB_PASSWORD=your_password_here

# JWT配置
JWT_SECRET=your_jwt_secret_here
JWT_EXPIRATION=86400000

# SAP HANA配置 (可选)
HANA_HOST=hana.example.com
HANA_PORT=30015

# Redis配置 (可选)
REDIS_HOST=localhost
REDIS_PORT=6379
```

### 2.4 启动服务

```bash
docker-compose up -d
```

### 2.5 验证部署

```bash
# 检查服务状态
docker-compose ps

# 查看日志
docker-compose logs -f backend
docker-compose logs -f frontend

# 访问前端
open http://localhost

# 健康检查
curl http://localhost:8080/api/health
```

## 三、详细配置

### 3.1 MySQL配置

```yaml
# docker-compose.yml 中的 MySQL 配置
mysql:
  image: mysql:8.0
  environment:
    MYSQL_ROOT_PASSWORD: ${DB_PASSWORD}
    MYSQL_DATABASE: ${DB_NAME}
  volumes:
    - mysql_data:/var/lib/mysql
    - ./database/init.sql:/docker-entrypoint-initdb.d/init.sql
    - ./database/migration_v2.sql:/docker-entrypoint-initdb.d/migration_v2.sql
  ports:
    - "3306:3306"
```

### 3.2 后端配置

```yaml
# 后端 Spring Boot 配置
spring:
  datasource:
    url: jdbc:mysql://${DB_HOST}:${DB_PORT}/${DB_NAME}?useSSL=false&serverTimezone=Asia/Shanghai
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      connection-timeout: 30000
      idle-timeout: 600000
      max-lifetime: 1800000

jwt:
  secret: ${JWT_SECRET}
  expiration: ${JWT_EXPIRATION:-86400000}
```

### 3.3 Nginx配置

```nginx
server {
    listen 80;
    server_name localhost;
    
    # 前端静态文件
    location / {
        root /usr/share/nginx/html;
        try_files $uri $uri/ /index.html;
    }
    
    # API代理
    location /api/ {
        proxy_pass http://backend:8080/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_connect_timeout 60s;
        proxy_send_timeout 60s;
        proxy_read_timeout 60s;
    }
    
    # WebSocket支持
    location /ws/ {
        proxy_pass http://backend:8080;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
    }
}
```

## 四、数据初始化

### 4.1 自动初始化

首次启动时，MySQL容器会自动执行SQL脚本：
- `init.sql` - 基础表结构
- `migration_v2.sql` - 新增表结构(报表/告警/SAP连接等)

### 4.2 手动初始化

```bash
# 1. 连接数据库
mysql -h localhost -u root -p

# 2. 执行初始化脚本
source database/init.sql
source database/migration_v2.sql
```

### 4.3 初始账号

```
用户名: admin
密码:   admin123
```

**重要**: 首次登录后请立即修改密码！

## 五、Docker Compose 配置

```yaml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    container_name: manageplat-mysql
    environment:
      MYSQL_ROOT_PASSWORD: ${DB_PASSWORD}
      MYSQL_DATABASE: ${DB_NAME}
    volumes:
      - mysql_data:/var/lib/mysql
      - ./database/init.sql:/docker-entrypoint-initdb.d/01-init.sql
      - ./database/migration_v2.sql:/docker-entrypoint-initdb.d/02-migration.sql
    ports:
      - "3306:3306"
    networks:
      - manageplat-net
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
      interval: 10s
      timeout: 5s
      retries: 5

  backend:
    build:
      context: ./backend
      dockerfile: Dockerfile
    container_name: manageplat-backend
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/${DB_NAME}?useSSL=false&serverTimezone=Asia/Shanghai
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: ${DB_PASSWORD}
      JWT_SECRET: ${JWT_SECRET}
      JWT_EXPIRATION: ${JWT_EXPIRATION:-86400000}
      HANA_HOST: ${HANA_HOST:-localhost}
      HANA_PORT: ${HANA_PORT:-30015}
    depends_on:
      mysql:
        condition: service_healthy
    networks:
      - manageplat-net
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/api/health"]
      interval: 30s
      timeout: 10s
      retries: 3

  frontend:
    build:
      context: ./frontend
      dockerfile: Dockerfile
    container_name: manageplat-frontend
    depends_on:
      - backend
    networks:
      - manageplat-net

  nginx:
    image: nginx:latest
    container_name: manageplat-nginx
    volumes:
      - ./nginx.conf:/etc/nginx/conf.d/default.conf
    ports:
      - "80:80"
      - "443:443"
    depends_on:
      - frontend
      - backend
    networks:
      - manageplat-net

networks:
  manageplat-net:
    driver: bridge

volumes:
  mysql_data:
```

## 六、运维命令

### 6.1 启动/停止

```bash
# 启动
docker-compose up -d

# 停止
docker-compose down

# 停止并删除数据卷
docker-compose down -v

# 重启
docker-compose restart

# 强制重建
docker-compose down
docker-compose build --no-cache
docker-compose up -d
```

### 6.2 日志查看

```bash
# 查看所有日志
docker-compose logs -f

# 查看指定服务日志
docker-compose logs -f backend
docker-compose logs -f frontend
docker-compose logs -f mysql

# 查看最近100行日志
docker-compose logs --tail=100 backend
```

### 6.3 数据库备份

```bash
# 备份
docker exec manageplat-mysql mysqldump -u root -p${DB_PASSWORD} manageplat > backup_$(date +%Y%m%d).sql

# 压缩备份
docker exec manageplat-mysql mysqldump -u root -p${DB_PASSWORD} manageplat | gzip > backup_$(date +%Y%m%d).sql.gz

# 恢复
docker exec -i manageplat-mysql mysql -u root -p${DB_PASSWORD} manageplat < backup.sql
```

### 6.4 进入容器

```bash
# 进入后端容器
docker exec -it manageplat-backend bash

# 进入MySQL容器
docker exec -it manageplat-mysql mysql -u root -p${DB_PASSWORD}
```

## 七、升级更新

### 7.1 代码更新

```bash
# 1. 拉取最新代码
git pull

# 2. 备份数据库
docker exec manageplat-mysql mysqldump -u root -p${DB_PASSWORD} manageplat > backup_$(date +%Y%m%d).sql

# 3. 重新构建
docker-compose build

# 4. 重启服务
docker-compose up -d

# 5. 执行数据库迁移
docker exec -i manageplat-mysql mysql -u root -p${DB_PASSWORD} manageplat < database/migration_v2.sql
```

### 7.2 滚动更新

```bash
# 逐个更新服务
docker-compose up -d --no-deps backend
docker-compose up -d --no-deps frontend
```

## 八、外部服务连接

### 8.1 SAP HANA连接

```yaml
hana:
  pools:
    - name: HANA_DEFAULT
      host: ${HANA_HOST}
      port: ${HANA_PORT:-30015}
      database: ${HANA_DATABASE:-HXE}
      username: ${HANA_USERNAME}
      password: ${HANA_PASSWORD}
      ssl:
        enabled: ${HANA_SSL_ENABLED:-false}
```

### 8.2 SAP RFC连接

```yaml
sap:
  rfc:
    ashost: ${SAP_ASHOST}
    sysnr: ${SAP_SYSNR}
    client: ${SAP_CLIENT}
    user: ${SAP_USER}
    password: ${SAP_PASSWORD}
```

### 8.3 邮件服务

```yaml
spring:
  mail:
    host: ${MAIL_HOST}
    port: ${MAIL_PORT:-465}
    username: ${MAIL_USERNAME}
    password: ${MAIL_PASSWORD}
    properties:
      mail:
        smtp:
          auth: true
          ssl:
            enable: true
```

### 8.4 短信服务

```yaml
sms:
  provider: aliyun  # aliyun | qcloud
  access-key: ${SMS_ACCESS_KEY}
  access-secret: ${SMS_ACCESS_SECRET}
  sign-name: ${SMS_SIGN_NAME}
```

## 九、常见问题

### Q1: MySQL启动失败

```bash
# 检查日志
docker-compose logs mysql

# 解决方案
# 1. 确保端口3306未被占用
lsof -i:3306

# 2. 删除旧数据卷后重新启动
docker-compose down -v
docker-compose up -d
```

### Q2: 后端无法连接数据库

```bash
# 检查网络
docker network inspect manageplat-manageplat-net

# 检查连接
docker exec manageplat-backend ping mysql

# 解决方案
# 确保DB_HOST配置为容器名
```

### Q3: 前端无法访问API

```bash
# 检查浏览器控制台网络请求
# 检查Nginx代理配置
docker exec manageplat-nginx cat /etc/nginx/conf.d/default.conf

# 解决方案
docker-compose restart nginx
```

### Q4: 内存溢出

```bash
# 调整JVM内存
# 在docker-compose.yml中
environment:
  JAVA_OPTS: -Xms512m -Xmx2g -XX:+UseG1GC
```

### Q5: 数据库连接超时

```yaml
# 增大连接超时时间
spring:
  datasource:
    hikari:
      connection-timeout: 60000
      idle-timeout: 600000
```

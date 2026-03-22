# WSL环境运行指南

## 环境要求

### WSL (SUSE/Linux)
- Java 17+ (已有: Java 21)
- unzip工具: `sudo zypper install -y unzip`
- Maven (使用Wrapper或安装)

### Windows
- Node.js 18+ (已有: D:\nvm4w\nodejs\node.exe)
- Docker Desktop (可选，用于容器化运行)

---

## 快速开始

### 方式1: Windows本地 + Docker MySQL (推荐)

```bash
# 1. 启动MySQL (Docker)
cd docker
docker-compose up -d mysql

# 2. 修改后端数据库配置 (使用MySQL)
# 编辑 backend/src/main/resources/application.yml
# 将H2配置改为MySQL配置

# 3. 在Windows运行后端
# 使用IDEA打开backend项目，运行Application.java

# 4. 在Windows运行前端
cd frontend
npm install
npm run dev
```

### 方式2: 纯Docker运行 (需要高性能)

```bash
cd docker
docker-compose -f docker-compose.dev.yml up -d
```

### 方式3: WSL原生 (需要安装工具)

```bash
# 安装依赖
sudo zypper install -y unzip curl

# 运行启动脚本
./start.sh install   # 安装依赖
./start.sh start     # 启动服务
```

---

## 服务端口

| 服务 | 端口 | 地址 |
|------|------|------|
| 后端API | 8080 | http://localhost:8080 |
| 前端 | 5173 | http://localhost:5173 |
| H2控制台 | 8080 | http://localhost:8080/h2-console |
| MySQL | 3306 | localhost:3306 |

---

## 调试命令

```bash
# 查看后端日志
docker logs -f manageplat-backend

# 查看前端日志  
docker logs -f manageplat-frontend

# 进入后端容器
docker exec -it manageplat-backend /bin/sh

# 重启服务
docker-compose -f docker-compose.dev.yml restart
```

---

## 常见问题

### Q: WSL中npm太慢
A: 使用Windows的Node.js，设置PATH

### Q: Maven下载太慢
A: 使用阿里云镜像
```bash
# 在 ~/.m2/settings.xml 中添加
<mirrors>
  <mirror>
    <id>aliyun</id>
    <url>https://maven.aliyun.com/repository/public</url>
  </mirror>
</mirrors>
```

### Q: 端口被占用
A: 检查占用端口的应用
```bash
netstat -ano | grep 8080
netstat -ano | grep 5173
```

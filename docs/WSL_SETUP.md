# WSL环境配置

## 当前环境状态

```
OS: SUSE Linux Enterprise Server 15 SP7
Java: OpenJDK 21 (已安装)
Node.js: Windows D:\nvm4w\nodejs (需配置PATH)
Maven: 未安装 (需要下载)
npm: Windows D:\nvm4w\nodejs (需配置PATH)
Docker: Docker Desktop (WSL无法直接访问)
```

---

## 解决方案

### 方案A: Windows本地运行 (推荐)

1. **MySQL (Docker)**
```powershell
cd docker
docker-compose up -d mysql
```

2. **后端 (IntelliJ IDEA)**
- 打开 `backend` 项目为Maven项目
- 安装Maven (如果IDEA没有内置)
- 运行 `PlatformApplication.java`

3. **前端 (命令行)**
```cmd
cd frontend
npm install
npm run dev
```

### 方案B: 配置WSL环境

1. **安装unzip**
```bash
sudo zypper install -y unzip
```

2. **安装Maven**
```bash
# 下载Maven
curl -o /tmp/maven.zip https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.6/apache-maven-3.9.6-bin.zip

# 解压
cd /tmp
unzip maven.zip
mv apache-maven-3.9.6 ~/maven

# 添加到PATH (添加到 ~/.bashrc)
export PATH="$HOME/maven/bin:$PATH"
source ~/.bashrc
```

3. **配置Node.js**
```bash
# 添加到 ~/.bashrc
export PATH="/mnt/d/nvm4w/nodejs:$PATH"
source ~/.bashrc

# 验证
node --version
npm --version
```

4. **运行服务**
```bash
./start.sh install
./start.sh start
```

### 方案C: 使用Docker (需配置)

1. **启用Docker in WSL**
```powershell
# 在PowerShell中运行 (需要管理员权限)
wsl --install docker-desktop
# 或者在Docker Desktop设置中启用WSL集成
```

2. **启动服务**
```bash
docker-compose -f docker-compose.dev.yml up -d
```

---

## 快速命令参考

```bash
# 安装WSL依赖
sudo zypper install -y unzip curl wget

# 检查工具
java -version
node --version  # 需要配置PATH
mvn --version   # 需要安装

# 运行后端 (Maven Wrapper)
cd backend
./mvnw spring-boot:run

# 运行前端
cd frontend
export PATH="/mnt/d/nvm4w/nodejs:$PATH"
npm run dev

# Docker命令
docker ps
docker logs -f manageplat-backend
docker-compose logs -f
```

---

## 文件说明

- `start.sh` - 启动脚本 (WSL原生运行)
- `docker/docker-compose.yml` - MySQL容器
- `docker/docker-compose.dev.yml` - 完整开发环境
- `docs/WSL_RUN_GUIDE.md` - 详细运行指南
- `docs/TEST_GUIDE.md` - 测试指南

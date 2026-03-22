================================================================================
                    ManagementPlat 启动指南
                    SAP统一管理平台 - 开发环境
================================================================================

一、环境要求
--------------------------------------------------------------------------------
- JDK 17+
- Node.js 18+
- Maven 3.9+ (如需要)
- Docker (可选，用于MySQL)

二、开发环境快速启动 (使用H2内存数据库)
--------------------------------------------------------------------------------

1. 编译后端
   cd backend
   mvn clean package -DskipTests

2. 启动后端服务 (端口: 8080)
   mvn spring-boot:run
   
   或直接运行jar包:
   java -jar target/manageplat-1.0.0.jar

3. 新开终端，启动前端 (端口: 3000)
   cd frontend
   npm install  (首次运行需要)
   npm run dev

三、Docker MySQL启动 (生产环境推荐)
--------------------------------------------------------------------------------

1. 进入docker目录
   cd docker

2. 启动MySQL (快速启动脚本)
   ./start-mysql.sh
   
   或手动启动:
   docker-compose up -d

3. 等待MySQL就绪
   docker exec manageplat-mysql mysqladmin ping -h localhost

4. 连接信息
   主机: localhost
   端口: 3306
   数据库: manageplat
   用户名: root
   密码: root123

5. 停止MySQL
   docker-compose down

四、使用MySQL替代H2
--------------------------------------------------------------------------------

1. 编辑 backend/src/main/resources/application.yml
   注释掉H2配置，取消注释MySQL配置

2. 或创建 application-dev-mysql.yml:
   spring:
     datasource:
       driver-class-name: com.mysql.cj.jdbc.Driver
       url: jdbc:mysql://localhost:3306/manageplat
       username: root
       password: root123

3. 运行MySQL初始化脚本
   docker exec -i manageplat-mysql mysql -u root -proot123 < ../database/init.sql

五、访问地址
--------------------------------------------------------------------------------
- 前端页面:  http://localhost:3000
- 后端API:   http://localhost:8080
- H2控制台:  http://localhost:8080/h2-console (仅dev profile)
- MySQL:     localhost:3306

六、登录账号
--------------------------------------------------------------------------------
用户名: admin
密码:   admin123

七、完整Docker部署 (所有服务)
--------------------------------------------------------------------------------
1. 进入docker目录
   cd docker

2. 配置环境变量
   cp ../.env.example .env
   编辑.env文件配置JWT_SECRET等

3. 启动所有服务
   docker-compose up -d --build

4. 查看服务状态
   docker-compose ps

5. 查看日志
   docker-compose logs -f

6. 停止所有服务
   docker-compose down

八、常见问题
--------------------------------------------------------------------------------
Q: Docker无法启动?
A: 在Windows上确保Docker Desktop已启动
   在Linux上运行: sudo service docker start

Q: 后端启动失败?
A: 检查端口8080是否被占用: lsof -i:8080

Q: 前端无法访问API?
A: 检查vite.config.js中proxy配置是否正确指向localhost:8080

Q: 登录失败?
A: 检查数据库是否正常初始化

Q: MySQL连接失败?
A: 检查Docker容器是否运行: docker ps
   检查端口: docker port manageplat-mysql

================================================================================
                           启动完成，Happy Coding!
================================================================================

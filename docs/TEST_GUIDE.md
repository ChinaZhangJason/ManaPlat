# 测试脚本 - 在Windows本地运行

## 前端测试

```bash
# 1. 进入前端目录
cd frontend

# 2. 安装依赖
npm install

# 3. 运行代码检查
npm run lint

# 4. 运行单元测试
npm test

# 5. 生成测试覆盖率报告
npm run test:coverage
```

## 后端测试

```bash
# 1. 进入后端目录
cd backend

# 2. 运行所有测试
mvn test

# 3. 运行单个测试类
mvn test -Dtest=ResultTest
mvn test -Dtest=AuthServiceTest
mvn test -Dtest=AlertServiceTest

# 4. 跳过需要Spring上下文的测试
mvn test -Dtest=!ManagementPlatApplicationTest

# 5. 生成覆盖率报告
mvn test jacoco:report

# 6. 编译打包
mvn clean package -DskipTests
```

## Windows下需要的工具

1. **Node.js** (前端)
   - 下载: https://nodejs.org/
   - 需要版本: v18+

2. **Maven** (后端)
   - 下载: https://maven.apache.org/download.cgi
   - 需要版本: 3.9+
   - 或使用IDEA自带的Maven

3. **Java** (后端)
   - 需要版本: Java 17+
   - 下载: https://adoptium.net/

## 在IDEA中运行测试

1. 打开后端项目为Maven项目
2. 打开前端项目
3. 运行测试：
   - 后端: 右键点击测试类 -> Run 'ClassNameTest'
   - 前端: 终端运行 npm test

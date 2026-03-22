#!/bin/bash

# ===========================================
# SAP统一管理平台 - 启动脚本 (WSL环境)
# ===========================================

# 设置颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

# 项目根目录
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
BACKEND_DIR="$PROJECT_ROOT/backend"
FRONTEND_DIR="$PROJECT_ROOT/frontend"

# 检查Node.js
check_node() {
    if command -v node &> /dev/null; then
        echo -e "${GREEN}✓ Node.js: $(node --version)${NC}"
        return 0
    else
        # 尝试从Windows路径添加
        if [ -f "/mnt/d/nvm4w/nodejs/node.exe" ]; then
            export PATH="/mnt/d/nvm4w/nodejs:$PATH"
            echo -e "${GREEN}✓ Node.js (Windows): $(node --version)${NC}"
            return 0
        fi
        echo -e "${RED}✗ Node.js not found${NC}"
        return 1
    fi
}

# 检查Java
check_java() {
    if command -v java &> /dev/null; then
        echo -e "${GREEN}✓ Java: $(java -version 2>&1 | head -1)${NC}"
        return 0
    else
        echo -e "${RED}✗ Java not found${NC}"
        return 1
    fi
}

# 检查Maven
check_maven() {
    if command -v mvn &> /dev/null; then
        echo -e "${GREEN}✓ Maven: $(mvn --version | head -1)${NC}"
        return 0
    else
        if [ -f "$BACKEND_DIR/mvnw" ]; then
            echo -e "${GREEN}✓ Maven Wrapper available${NC}"
            return 0
        fi
        echo -e "${YELLOW}⚠ Maven not found, will use Maven Wrapper${NC}"
        return 1
    fi
}

# 安装后端依赖
install_backend() {
    echo -e "\n${YELLOW}[1/4] 安装后端依赖...${NC}"
    cd "$BACKEND_DIR"
    
    if [ ! -f ".mvn/wrapper/maven-wrapper.jar" ]; then
        echo "下载Maven Wrapper..."
        curl -o .mvn/wrapper/maven-wrapper.jar \
            https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar
    fi
    
    chmod +x mvnw
    ./mvnw dependency:resolve -q
    echo -e "${GREEN}✓ 后端依赖安装完成${NC}"
}

# 安装前端依赖
install_frontend() {
    echo -e "\n${YELLOW}[2/4] 安装前端依赖...${NC}"
    cd "$FRONTEND_DIR"
    
    if [ -d "node_modules" ]; then
        echo -e "${GREEN}✓ node_modules已存在${NC}"
    else
        # 优先使用WSL的npm，如果没有则用Windows的
        if command -v npm &> /dev/null; then
            npm install
        elif [ -f "/mnt/d/nvm4w/nodejs/npm" ]; then
            export PATH="/mnt/d/nvm4w/nodejs:$PATH"
            npm install
        else
            echo -e "${RED}✗ npm not found${NC}"
            return 1
        fi
    fi
    echo -e "${GREEN}✓ 前端依赖安装完成${NC}"
}

# 启动后端服务
start_backend() {
    echo -e "\n${YELLOW}[3/4] 启动后端服务 (端口8080)...${NC}"
    cd "$BACKEND_DIR"
    
    if command -v mvn &> /dev/null; then
        mvn spring-boot:run &
    else
        chmod +x mvnw
        ./mvnw spring-boot:run &
    fi
    
    BACKEND_PID=$!
    echo $BACKEND_PID > /tmp/manageplat_backend.pid
    echo -e "${GREEN}✓ 后端服务启动中 (PID: $BACKEND_PID)${NC}"
    echo -e "${GREEN}  访问地址: http://localhost:8080${NC}"
    echo -e "${GREEN}  H2控制台: http://localhost:8080/h2-console${NC}"
}

# 启动前端服务
start_frontend() {
    echo -e "\n${YELLOW}[4/4] 启动前端服务 (端口5173)...${NC}"
    cd "$FRONTEND_DIR"
    
    # 设置Node.js路径
    if ! command -v npm &> /dev/null && [ -f "/mnt/d/nvm4w/nodejs/npm" ]; then
        export PATH="/mnt/d/nvm4w/nodejs:$PATH"
    fi
    
    npm run dev &
    FRONTEND_PID=$!
    echo $FRONTEND_PID > /tmp/manageplat_frontend.pid
    echo -e "${GREEN}✓ 前端服务启动中 (PID: $FRONTEND_PID)${NC}"
    echo -e "${GREEN}  访问地址: http://localhost:5173${NC}"
}

# 停止服务
stop_services() {
    echo -e "\n${YELLOW}停止服务...${NC}"
    
    if [ -f /tmp/manageplat_backend.pid ]; then
        kill $(cat /tmp/manageplat_backend.pid) 2>/dev/null
        rm /tmp/manageplat_backend.pid
        echo -e "${GREEN}✓ 后端服务已停止${NC}"
    fi
    
    if [ -f /tmp/manageplat_frontend.pid ]; then
        kill $(cat /tmp/manageplat_frontend.pid) 2>/dev/null
        rm /tmp/manageplat_frontend.pid
        echo -e "${GREEN}✓ 前端服务已停止${NC}"
    fi
}

# 显示状态
show_status() {
    echo -e "\n${YELLOW}=== 环境检查 ===${NC}"
    check_java
    check_node
    check_maven
    
    echo -e "\n${YELLOW}=== 端口检查 ===${NC}"
    echo "后端 (8080): $(lsof -i :8080 2>/dev/null | grep LISTEN || echo '未运行')"
    echo "前端 (5173): $(lsof -i :5173 2>/dev/null | grep LISTEN || echo '未运行')"
}

# 帮助信息
show_help() {
    echo "用法: $0 [命令]"
    echo ""
    echo "命令:"
    echo "  install    安装所有依赖"
    echo "  start      启动所有服务"
    echo "  stop       停止所有服务"
    echo "  restart    重启所有服务"
    echo "  status     查看服务状态"
    echo "  backend    仅启动后端"
    echo "  frontend   仅启动前端"
    echo "  help       显示帮助"
}

# 主逻辑
case "${1:-help}" in
    install)
        install_backend
        install_frontend
        ;;
    start)
        show_status
        install_backend
        install_frontend
        start_backend
        sleep 5
        start_frontend
        echo -e "\n${GREEN}=== 所有服务已启动 ===${NC}"
        echo "后端: http://localhost:8080"
        echo "前端: http://localhost:5173"
        ;;
    stop)
        stop_services
        ;;
    restart)
        stop_services
        sleep 2
        $0 start
        ;;
    status)
        show_status
        ;;
    backend)
        show_status
        install_backend
        start_backend
        ;;
    frontend)
        show_status
        install_frontend
        start_frontend
        ;;
    help|*)
        show_help
        ;;
esac

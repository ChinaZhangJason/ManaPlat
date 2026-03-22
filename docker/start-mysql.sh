#!/bin/bash
#===============================================
# MySQL Docker启动脚本
# ManagementPlat - SAP统一管理平台
#===============================================

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}   ManagementPlat MySQL 启动脚本${NC}"
echo -e "${GREEN}========================================${NC}"

# 检查Docker
if ! command -v docker &> /dev/null; then
    echo -e "${RED}错误: Docker未安装${NC}"
    exit 1
fi

if ! docker info &> /dev/null; then
    echo -e "${RED}错误: Docker daemon未运行${NC}"
    echo -e "${YELLOW}请运行: sudo service docker start${NC}"
    exit 1
fi

# 创建.env文件
if [ ! -f ".env" ]; then
    echo -e "${YELLOW}创建环境配置文件...${NC}"
    cp mysql/.env.example .env
    echo -e "${GREEN}已创建.env文件，请编辑配置${NC}"
fi

# 停止并删除旧容器
echo -e "${YELLOW}清理旧容器...${NC}"
docker-compose down -v 2>/dev/null || true

# 启动MySQL
echo -e "${YELLOW}启动MySQL容器...${NC}"
docker-compose up -d

# 等待MySQL就绪
echo -e "${YELLOW}等待MySQL启动...${NC}"
for i in {1..30}; do
    if docker exec manageplat-mysql mysqladmin ping -h localhost --silent 2>/dev/null; then
        echo -e "${GREEN}MySQL启动成功!${NC}"
        break
    fi
    if [ $i -eq 30 ]; then
        echo -e "${RED}MySQL启动超时${NC}"
        exit 1
    fi
    sleep 1
done

# 显示连接信息
echo ""
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}   MySQL 连接信息${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""
echo "  主机: localhost"
echo "  端口: 3306"
echo "  数据库: manageplat"
echo "  用户名: root"
echo "  密码: root123"
echo ""
echo -e "${GREEN}JDBC连接字符串:${NC}"
echo "  jdbc:mysql://localhost:3306/manageplat"
echo ""
echo -e "${YELLOW}H2配置替换示例 (application-prod.yml):${NC}"
echo "  spring:"
echo "    datasource:"
echo "      url: jdbc:mysql://localhost:3306/manageplat"
echo "      username: root"
echo "      password: root123"
echo ""
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}   启动完成!${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""
echo "命令:"
echo "  查看日志: docker-compose logs -f mysql"
echo "  停止服务: docker-compose down"
echo "  进入MySQL: docker exec -it manageplat-mysql mysql -u root -p"
echo ""

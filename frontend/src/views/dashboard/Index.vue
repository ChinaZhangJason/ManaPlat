<template>
  <div class="dashboard">
    <div class="dashboard-header">
      <div class="header-left">
        <h2>仪表盘</h2>
        <p class="welcome-text">欢迎回来，管理员</p>
      </div>
      <div class="header-right">
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          size="default"
        />
      </div>
    </div>

    <el-row :gutter="20" class="stat-row">
      <el-col :xs="24" :sm="12" :md="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-info">
              <p class="stat-label">用户总数</p>
              <p class="stat-value">{{ statsData.userCount }}</p>
              <p class="stat-trend up">
                <el-icon><Top /></el-icon>
                <span>12.5%</span>
                <span class="trend-text">较上周</span>
              </p>
            </div>
            <div class="stat-icon-box blue">
              <el-icon size="32"><User /></el-icon>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="12" :md="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-info">
              <p class="stat-label">SAP系统</p>
              <p class="stat-value">{{ statsData.sapCount }}</p>
              <p class="stat-trend up">
                <el-icon><Top /></el-icon>
                <span>8.3%</span>
                <span class="trend-text">较上周</span>
              </p>
            </div>
            <div class="stat-icon-box green">
              <el-icon size="32"><Box /></el-icon>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="12" :md="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-info">
              <p class="stat-label">监控指标</p>
              <p class="stat-value">{{ statsData.monitorCount }}</p>
              <p class="stat-trend down">
                <el-icon><Bottom /></el-icon>
                <span>3.2%</span>
                <span class="trend-text">较上周</span>
              </p>
            </div>
            <div class="stat-icon-box orange">
              <el-icon size="32"><Monitor /></el-icon>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="12" :md="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-info">
              <p class="stat-label">告警数量</p>
              <p class="stat-value">{{ statsData.alertCount }}</p>
              <p class="stat-trend neutral">
                <el-icon><Minus /></el-icon>
                <span>0%</span>
                <span class="trend-text">较上周</span>
              </p>
            </div>
            <div class="stat-icon-box red">
              <el-icon size="32"><Bell /></el-icon>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="quick-actions">
      <el-col :span="24">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>快捷操作</span>
            </div>
          </template>
          <div class="action-buttons">
            <el-button type="primary" @click="handleAction('addUser')">
              <el-icon><Plus /></el-icon>
              添加用户
            </el-button>
            <el-button type="success" @click="handleAction('addSystem')">
              <el-icon><Plus /></el-icon>
              添加系统
            </el-button>
            <el-button type="warning" @click="handleAction('viewMonitor')">
              <el-icon><Monitor /></el-icon>
              查看监控
            </el-button>
            <el-button type="info" @click="handleAction('viewAlert')">
              <el-icon><Bell /></el-icon>
              告警管理
            </el-button>
            <el-button @click="handleAction('refresh')">
              <el-icon><Refresh /></el-icon>
              刷新数据
            </el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="chart-section">
      <el-col :xs="24" :lg="16">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>系统状态概览</span>
              <el-radio-group v-model="chartTimeRange" size="small">
                <el-radio-button label="week">本周</el-radio-button>
                <el-radio-button label="month">本月</el-radio-button>
                <el-radio-button label="year">本年</el-radio-button>
              </el-radio-group>
            </div>
          </template>
          <div ref="mainChartRef" style="height: 350px;"></div>
        </el-card>
      </el-col>

      <el-col :xs="24" :lg="8">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>系统分布</span>
            </div>
          </template>
          <div ref="pieChartRef" style="height: 350px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="bottom-section">
      <el-col :xs="24" :lg="12">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>最近活动</span>
              <el-button type="primary" link @click="viewAllActivities">
                查看全部
                <el-icon><ArrowRight /></el-icon>
              </el-button>
            </div>
          </template>
          <el-table :data="recentActivities" style="width: 100%" :show-header="true">
            <el-table-column prop="time" label="时间" width="160" />
            <el-table-column prop="user" label="用户" width="120" />
            <el-table-column prop="action" label="操作" />
            <el-table-column prop="status" label="状态" width="80" align="center">
              <template #default="{ row }">
                <el-tag :type="row.statusType" size="small">{{ row.status }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>

      <el-col :xs="24" :lg="12">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>SAP系统状态</span>
              <el-button type="primary" link @click="viewAllSystems">
                查看全部
                <el-icon><ArrowRight /></el-icon>
              </el-button>
            </div>
          </template>
          <div class="system-list">
            <div v-for="system in sapSystems" :key="system.id" class="system-item">
              <div class="system-info">
                <el-icon :color="system.statusColor" size="20">
                  <CircleCheck v-if="system.status === 'running'" />
                  <CircleClose v-else-if="system.status === 'error'" />
                  <Loading v-else />
                </el-icon>
                <div class="system-detail">
                  <p class="system-name">{{ system.name }}</p>
                  <p class="system-desc">{{ system.description }}</p>
                </div>
              </div>
              <div class="system-status">
                <el-tag :type="system.statusType" size="small">{{ system.statusText }}</el-tag>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts/core'
import { LineChart, PieChart } from 'echarts/charts'
import { GridComponent, TooltipComponent, LegendComponent } from 'echarts/components'
import { CanvasRenderer } from 'echarts/renderers'
import {
  User,
  Box,
  Monitor,
  Bell,
  Top,
  Bottom,
  Minus,
  Plus,
  Refresh,
  ArrowRight,
  CircleCheck,
  CircleClose,
  Loading
} from '@element-plus/icons-vue'

echarts.use([CanvasRenderer, LineChart, PieChart, GridComponent, TooltipComponent, LegendComponent])

const router = useRouter()
const dateRange = ref([])
const chartTimeRange = ref('week')
const mainChartRef = ref(null)
const pieChartRef = ref(null)
let mainChart = null
let pieChart = null

const statsData = reactive({
  userCount: 156,
  sapCount: 12,
  monitorCount: 324,
  alertCount: 3
})

const recentActivities = ref([
  { time: '2026-03-19 14:30', user: 'admin', action: '登录系统', status: '成功', statusType: 'success' },
  { time: '2026-03-19 14:25', user: 'admin', action: '创建监控任务', status: '成功', statusType: 'success' },
  { time: '2026-03-19 14:20', user: 'zhang', action: '修改系统配置', status: '成功', statusType: 'success' },
  { time: '2026-03-19 14:15', user: 'li', action: '执行SQL查询', status: '失败', statusType: 'danger' },
  { time: '2026-03-19 14:10', user: 'admin', action: '导出报表', status: '成功', statusType: 'success' }
])

const sapSystems = ref([
  { id: 1, name: 'SAP PRD', description: '生产系统', status: 'running', statusText: '运行中', statusColor: '#67c23a', statusType: 'success' },
  { id: 2, name: 'SAP QAS', description: '测试系统', status: 'running', statusText: '运行中', statusColor: '#67c23a', statusType: 'success' },
  { id: 3, name: 'SAP DEV', description: '开发系统', status: 'running', statusText: '运行中', statusColor: '#67c23a', statusType: 'success' },
  { id: 4, name: 'HANA DB', description: 'HANA数据库', status: 'error', statusText: '异常', statusColor: '#f56c6c', statusType: 'danger' },
  { id: 5, name: 'S/4 HANA', description: 'S/4 HANA系统', status: 'running', statusText: '运行中', statusColor: '#67c23a', statusType: 'success' }
])

const initMainChart = () => {
  if (!mainChartRef.value) return
  
  mainChart = echarts.init(mainChartRef.value)
  
  const option = {
    tooltip: {
      trigger: 'axis',
      backgroundColor: 'rgba(255, 255, 255, 0.95)',
      borderColor: '#eee',
      borderWidth: 1,
      textStyle: { color: '#333' }
    },
    legend: {
      data: ['用户活跃', '系统访问', '告警数量'],
      bottom: 0
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '15%',
      top: '10%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '用户活跃',
        type: 'line',
        smooth: true,
        data: [820, 932, 901, 1234, 1290, 1330, 1320],
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(64, 158, 255, 0.3)' },
            { offset: 1, color: 'rgba(64, 158, 255, 0.05)' }
          ])
        },
        lineStyle: { color: '#409eff' },
        itemStyle: { color: '#409eff' }
      },
      {
        name: '系统访问',
        type: 'line',
        smooth: true,
        data: [620, 732, 801, 934, 1190, 1230, 1220],
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(103, 194, 58, 0.3)' },
            { offset: 1, color: 'rgba(103, 194, 58, 0.05)' }
          ])
        },
        lineStyle: { color: '#67c23a' },
        itemStyle: { color: '#67c23a' }
      },
      {
        name: '告警数量',
        type: 'line',
        smooth: true,
        data: [120, 132, 101, 134, 90, 130, 110],
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(245, 108, 108, 0.3)' },
            { offset: 1, color: 'rgba(245, 108, 108, 0.05)' }
          ])
        },
        lineStyle: { color: '#f56c6c' },
        itemStyle: { color: '#f56c6c' }
      }
    ]
  }
  
  mainChart.setOption(option)
}

const initPieChart = () => {
  if (!pieChartRef.value) return
  
  pieChart = echarts.init(pieChartRef.value)
  
  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 'left',
      bottom: 'center'
    },
    series: [
      {
        name: '系统类型',
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 16,
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: [
          { value: 5, name: 'S/4 HANA', itemStyle: { color: '#409eff' } },
          { value: 3, name: 'ECC', itemStyle: { color: '#67c23a' } },
          { value: 2, name: 'BW', itemStyle: { color: '#e6a23c' } },
          { value: 1, name: 'CRM', itemStyle: { color: '#f56c6c' } },
          { value: 1, name: '其他', itemStyle: { color: '#909399' } }
        ]
      }
    ]
  }
  
  pieChart.setOption(option)
}

const handleResize = () => {
  mainChart?.resize()
  pieChart?.resize()
}

const handleAction = (type) => {
  const actions = {
    addUser: () => router.push('/system/users'),
    addSystem: () => router.push('/monitor'),
    viewMonitor: () => router.push('/monitor'),
    viewAlert: () => router.push('/monitor'),
    refresh: () => {
      ElMessage.success('数据已刷新')
    }
  }
  
  actions[type]?.()
}

const viewAllActivities = () => {
  ElMessage.info('查看全部活动')
}

const viewAllSystems = () => {
  router.push('/monitor')
}

onMounted(() => {
  initMainChart()
  initPieChart()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  mainChart?.dispose()
  pieChart?.dispose()
})
</script>

<style scoped>
.dashboard {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 60px);
}

.dashboard-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  flex-wrap: wrap;
  gap: 15px;
}

.header-left h2 {
  margin: 0 0 5px 0;
  font-size: 24px;
  color: #303133;
}

.welcome-text {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.stat-row {
  margin-bottom: 20px;
}

.stat-card {
  margin-bottom: 15px;
}

.stat-card :deep(.el-card__body) {
  padding: 20px;
}

.stat-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.stat-label {
  margin: 0 0 8px 0;
  font-size: 14px;
  color: #909399;
}

.stat-value {
  margin: 0 0 8px 0;
  font-size: 28px;
  font-weight: 600;
  color: #303133;
}

.stat-trend {
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 12px;
  margin: 0;
}

.stat-trend.up {
  color: #67c23a;
}

.stat-trend.down {
  color: #f56c6c;
}

.stat-trend.neutral {
  color: #909399;
}

.trend-text {
  color: #909399;
  margin-left: 5px;
}

.stat-icon-box {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
}

.stat-icon-box.blue {
  background: linear-gradient(135deg, #409eff, #66b1ff);
}

.stat-icon-box.green {
  background: linear-gradient(135deg, #67c23a, #85ce61);
}

.stat-icon-box.orange {
  background: linear-gradient(135deg, #e6a23c, #ebb563);
}

.stat-icon-box.red {
  background: linear-gradient(135deg, #f56c6c, #f78989);
}

.quick-actions {
  margin-bottom: 20px;
}

.quick-actions :deep(.el-card__body) {
  padding: 15px 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header span {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.action-buttons {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.chart-section {
  margin-bottom: 20px;
}

.bottom-section {
  margin-bottom: 20px;
}

.system-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.system-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  background-color: #f9fafb;
  border-radius: 6px;
  transition: all 0.3s;
}

.system-item:hover {
  background-color: #f0f2f5;
}

.system-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.system-detail {
  display: flex;
  flex-direction: column;
}

.system-name {
  margin: 0;
  font-size: 14px;
  font-weight: 500;
  color: #303133;
}

.system-desc {
  margin: 0;
  font-size: 12px;
  color: #909399;
}

@media (max-width: 768px) {
  .dashboard {
    padding: 15px;
  }
  
  .dashboard-header {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .header-left h2 {
    font-size: 20px;
  }
  
  .stat-value {
    font-size: 24px;
  }
  
  .stat-icon-box {
    width: 50px;
    height: 50px;
  }
  
  .action-buttons {
    flex-direction: column;
  }
  
  .action-buttons .el-button {
    width: 100%;
  }
}
</style>

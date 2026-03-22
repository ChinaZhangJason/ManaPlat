<template>
  <div class="monitoring-container">
    <el-card class="filter-card">
      <div class="filter-row">
        <div class="filter-item">
          <span class="filter-label">SAP系统:</span>
          <el-select
            v-model="selectedSystems"
            multiple
            placeholder="请选择SAP系统"
            style="width: 280px"
            collapse-tags
            collapse-tags-tooltip
          >
            <el-option
              v-for="system in sapSystems"
              :key="system.id"
              :label="system.name"
              :value="system.id"
            />
          </el-select>
        </div>
        
        <div class="filter-item">
          <span class="filter-label">指标类型:</span>
          <el-select v-model="metricType" placeholder="请选择指标" style="width: 180px">
            <el-option label="CPU使用率" value="cpu" />
            <el-option label="内存使用率" value="memory" />
            <el-option label="响应时间" value="response_time" />
            <el-option label="请求数" value="requests" />
            <el-option label="错误率" value="error_rate" />
          </el-select>
        </div>
        
        <div class="filter-item">
          <span class="filter-label">时间范围:</span>
          <el-select v-model="timeRange" placeholder="请选择时间范围" style="width: 160px">
            <el-option label="最近30分钟" value="30m" />
            <el-option label="最近1小时" value="1h" />
            <el-option label="最近6小时" value="6h" />
            <el-option label="最近24小时" value="24h" />
            <el-option label="最近7天" value="7d" />
            <el-option label="最近30天" value="30d" />
          </el-select>
        </div>
        
        <div class="filter-actions">
          <el-button type="primary" :loading="loading" @click="queryData">
            <el-icon v-if="!loading"><Search /></el-icon>
            查询
          </el-button>
          <el-button @click="resetFilters">
            <el-icon><RefreshLeft /></el-icon>
            重置
          </el-button>
        </div>
      </div>
    </el-card>

    <el-row :gutter="20" class="chart-row">
      <el-col :span="24">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>{{ metricTitle }}</span>
              <div class="header-actions">
                <el-button size="small" @click="refreshChart">
                  <el-icon><Refresh /></el-icon>
                </el-button>
              </div>
            </div>
          </template>
          
          <div v-loading="loading" element-loading-text="加载数据中...">
            <LineChart
              v-if="chartData.series.length > 0"
              ref="chartRef"
              :height="chartHeight"
              :x-axis-data="chartData.xAxis"
              :series="chartData.series"
              :title="metricTitle"
              :show-legend="selectedSystems.length > 1"
              :y-axis-name="yAxisName"
              :smooth="true"
              :show-area="false"
            />
            <el-empty v-else description="请选择SAP系统并查询数据" />
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="stats-row">
      <el-col :span="6" v-for="stat in currentStats" :key="stat.label">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-label">{{ stat.label }}</div>
            <div class="stat-value" :style="{ color: stat.color }">{{ stat.value }}</div>
            <div class="stat-trend" :class="stat.trend">
              <el-icon v-if="stat.trend === 'up'"><Top /></el-icon>
              <el-icon v-else-if="stat.trend === 'down'"><Bottom /></el-icon>
              <span>{{ stat.change }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="table-row">
      <el-col :span="24">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>详细数据</span>
              <el-button type="primary" size="small" @click="exportData">
                <el-icon><Download /></el-icon>
                导出
              </el-button>
            </div>
          </template>
          
          <el-table :data="tableData" v-loading="loading" stripe>
            <el-table-column prop="timestamp" label="时间" width="180" />
            <el-table-column prop="system" label="系统" width="150" />
            <el-table-column prop="cpu" label="CPU (%)" width="120">
              <template #default="{ row }">
                <el-progress
                  :percentage="row.cpu"
                  :color="getProgressColor(row.cpu)"
                  :stroke-width="10"
                />
              </template>
            </el-table-column>
            <el-table-column prop="memory" label="内存 (%)" width="120">
              <template #default="{ row }">
                <el-progress
                  :percentage="row.memory"
                  :color="getProgressColor(row.memory)"
                  :stroke-width="10"
                />
              </template>
            </el-table-column>
            <el-table-column prop="responseTime" label="响应时间 (ms)" width="140" />
            <el-table-column prop="requests" label="请求数" width="100" />
            <el-table-column prop="errorRate" label="错误率 (%)" width="100" />
            <el-table-column label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="row.status === 'normal' ? 'success' : 'danger'" size="small">
                  {{ row.status === 'normal' ? '正常' : '异常' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
          
          <div class="pagination-wrapper">
            <el-pagination
              v-model:current-page="currentPage"
              v-model:page-size="pageSize"
              :total="total"
              :page-sizes="[10, 20, 50, 100]"
              layout="total, sizes, prev, pager, next, jumper"
              @size-change="handleSizeChange"
              @current-change="handlePageChange"
            />
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, onBeforeUnmount } from 'vue'
import { ElMessage } from 'element-plus'
import LineChart from '@/components/charts/LineChart.vue'
import { Search, Refresh, RefreshLeft, Download, Top, Bottom } from '@element-plus/icons-vue'

const selectedSystems = ref([])
const metricType = ref('cpu')
const timeRange = ref('1h')
const loading = ref(false)
const chartRef = ref(null)
const chartHeight = ref('400px')

const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const sapSystems = ref([
  { id: 1, name: 'SAP PRD - 生产系统' },
  { id: 2, name: 'SAP QAS - 测试系统' },
  { id: 3, name: 'SAP DEV - 开发系统' },
  { id: 4, name: 'SAP Sandbox' }
])

const tableData = ref([])

const metricTitle = computed(() => {
  const titles = {
    cpu: 'CPU 使用率趋势',
    memory: '内存使用率趋势',
    response_time: '响应时间趋势',
    requests: '请求数趋势',
    error_rate: '错误率趋势'
  }
  return titles[metricType.value] || '指标趋势'
})

const yAxisName = computed(() => {
  const names = {
    cpu: '百分比 (%)',
    memory: '百分比 (%)',
    response_time: '毫秒 (ms)',
    requests: '请求数',
    error_rate: '百分比 (%)'
  }
  return names[metricType.value] || ''
})

const generateMockData = () => {
  const hours = 12
  const xAxis = []
  const now = new Date()
  
  for (let i = hours - 1; i >= 0; i--) {
    const time = new Date(now.getTime() - i * 60 * 60 * 1000)
    xAxis.push(time.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' }))
  }
  
  const selectedNames = selectedSystems.value.length > 0
    ? sapSystems.value.filter(s => selectedSystems.value.includes(s.id)).map(s => s.name)
    : [sapSystems.value[0].name]
  
  const series = selectedNames.map((name, index) => {
    const baseValue = metricType.value === 'cpu' ? 40 + index * 10 : 
                      metricType.value === 'memory' ? 60 + index * 5 :
                      metricType.value === 'response_time' ? 200 + index * 50 :
                      metricType.value === 'requests' ? 1000 + index * 200 :
                      2 + index * 0.5
    
    const data = Array.from({ length: hours }, () => {
      const variance = metricType.value === 'requests' ? baseValue * 0.3 : baseValue * 0.2
      return Math.max(0, baseValue + (Math.random() - 0.5) * variance).toFixed(2)
    })
    
    return { name, data }
  })
  
  return { xAxis, series }
}

const chartData = reactive({
  xAxis: [],
  series: []
})

const currentStats = computed(() => {
  const stats = [
    { label: '平均' + (metricType.value === 'cpu' ? 'CPU' : metricType.value === 'memory' ? '内存' : '指标'), 
      value: calculateAverage(), color: '#409eff', trend: 'stable', change: '0%' },
    { label: '最大值', value: calculateMax(), color: '#f56c6c', trend: 'up', change: '+5%' },
    { label: '最小值', value: calculateMin(), color: '#67c23a', trend: 'down', change: '-3%' },
    { label: '当前值', value: calculateCurrent(), color: '#e6a23c', trend: 'up', change: '+2%' }
  ]
  return stats
})

const calculateAverage = () => {
  if (chartData.series.length === 0) return '0'
  const allValues = chartData.series.flatMap(s => s.data.map(Number))
  return (allValues.reduce((a, b) => a + b, 0) / allValues.length).toFixed(2)
}

const calculateMax = () => {
  if (chartData.series.length === 0) return '0'
  const allValues = chartData.series.flatMap(s => s.data.map(Number))
  return Math.max(...allValues).toFixed(2)
}

const calculateMin = () => {
  if (chartData.series.length === 0) return '0'
  const allValues = chartData.series.flatMap(s => s.data.map(Number))
  return Math.min(...allValues).toFixed(2)
}

const calculateCurrent = () => {
  if (chartData.series.length === 0) return '0'
  return chartData.series[0].data[chartData.series[0].data.length - 1]
}

const getProgressColor = (value) => {
  if (value < 50) return '#67c23a'
  if (value < 80) return '#e6a23c'
  return '#f56c6c'
}

const queryData = async () => {
  if (selectedSystems.value.length === 0) {
    ElMessage.warning('请至少选择一个SAP系统')
    return
  }
  
  loading.value = true
  try {
    await new Promise(resolve => setTimeout(resolve, 800))
    
    const mockData = generateMockData()
    chartData.xAxis = mockData.xAxis
    chartData.series = mockData.series
    
    generateTableData()
    total.value = tableData.value.length
    
    ElMessage.success('数据加载成功')
  } catch (error) {
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

const generateTableData = () => {
  const now = new Date()
  tableData.value = []
  
  for (let i = 0; i < 20; i++) {
    const time = new Date(now.getTime() - i * 5 * 60 * 1000)
    const systemIndex = i % selectedSystems.value.length || 0
    const systemId = selectedSystems.value[systemIndex] || sapSystems.value[0].id
    const systemName = sapSystems.value.find(s => s.id === systemId)?.name || '未知系统'
    
    tableData.value.push({
      timestamp: time.toLocaleString('zh-CN'),
      system: systemName,
      cpu: Math.floor(Math.random() * 40 + 30),
      memory: Math.floor(Math.random() * 30 + 50),
      responseTime: Math.floor(Math.random() * 300 + 100),
      requests: Math.floor(Math.random() * 500 + 500),
      errorRate: (Math.random() * 3).toFixed(2),
      status: Math.random() > 0.1 ? 'normal' : 'abnormal'
    })
  }
}

const resetFilters = () => {
  selectedSystems.value = []
  metricType.value = 'cpu'
  timeRange.value = '1h'
  chartData.xAxis = []
  chartData.series = []
  tableData.value = []
  total.value = 0
}

const refreshChart = () => {
  if (chartData.series.length > 0) {
    queryData()
  } else {
    ElMessage.info('请先查询数据')
  }
}

const exportData = () => {
  ElMessage.success('数据导出功能开发中')
}

const handleSizeChange = (val) => {
  pageSize.value = val
}

const handlePageChange = (val) => {
  currentPage.value = val
}

let refreshTimer = null

onMounted(() => {
  if (selectedSystems.value.length > 0) {
    queryData()
  }
  
  refreshTimer = setInterval(() => {
    if (chartData.series.length > 0 && !loading.value) {
      const mockData = generateMockData()
      chartData.xAxis = mockData.xAxis
      chartData.series = mockData.series
    }
  }, 30000)
})

onBeforeUnmount(() => {
  if (refreshTimer) {
    clearInterval(refreshTimer)
  }
})
</script>

<style scoped>
.monitoring-container {
  padding: 0;
}

.filter-card {
  margin-bottom: 20px;
}

.filter-row {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  align-items: center;
}

.filter-item {
  display: flex;
  align-items: center;
  gap: 10px;
}

.filter-label {
  font-weight: 500;
  color: #606266;
  white-space: nowrap;
}

.filter-actions {
  margin-left: auto;
  display: flex;
  gap: 10px;
}

.chart-row {
  margin-bottom: 20px;
}

.chart-card {
  min-height: 450px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.stats-row {
  margin-bottom: 20px;
}

.stat-card {
  cursor: pointer;
  transition: transform 0.2s;
}

.stat-card:hover {
  transform: translateY(-2px);
}

.stat-content {
  text-align: center;
  padding: 10px 0;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 8px;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 8px;
}

.stat-trend {
  font-size: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
}

.stat-trend.up {
  color: #67c23a;
}

.stat-trend.down {
  color: #f56c6c;
}

.stat-trend.stable {
  color: #909399;
}

.table-row {
  margin-bottom: 20px;
}

.pagination-wrapper {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

:deep(.el-progress) {
  width: 90%;
}

:deep(.el-card__header) {
  padding: 12px 20px;
  background: #fafafa;
}

:deep(.el-select) {
  --el-select-border-color-hover: #409eff;
}
</style>

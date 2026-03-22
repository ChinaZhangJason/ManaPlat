<template>
  <div class="sql-test-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>SQL 测试工具</span>
          <el-select v-model="selectedDatasource" placeholder="选择数据源" style="width: 200px">
            <el-option label="MySQL" value="mysql" />
            <el-option label="HANA" value="hana" />
          </el-select>
        </div>
      </template>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-card shadow="never" class="editor-card">
            <template #header>SQL 编辑器</template>
            <el-input
              v-model="sqlContent"
              type="textarea"
              :rows="15"
              placeholder="请输入SQL语句..."
              class="sql-editor"
            />
            <div class="editor-actions">
              <el-button type="primary" @click="executeSql" :loading="loading">
                执行 (Ctrl+Enter)
              </el-button>
              <el-button @click="formatSql">格式化</el-button>
              <el-button @click="clearSql">清空</el-button>
            </div>
          </el-card>
        </el-col>
        
        <el-col :span="12">
          <el-card shadow="never" class="history-card">
            <template #header>
              <span>执行历史</span>
            </template>
            <div class="history-list">
              <div 
                v-for="(item, index) in historyList" 
                :key="index"
                class="history-item"
                @click="loadHistory(item)"
              >
                <el-tag :type="item.success ? 'success' : 'danger'" size="small">
                  {{ item.success ? '成功' : '失败' }}
                </el-tag>
                <span class="history-sql">{{ item.sql.substring(0, 50) }}...</span>
                <span class="history-time">{{ item.time }}</span>
              </div>
              <el-empty v-if="historyList.length === 0" description="暂无历史记录" />
            </div>
          </el-card>
        </el-col>
      </el-row>
      
      <el-card shadow="never" class="result-card" style="margin-top: 20px">
        <template #header>
          <div class="result-header">
            <span>执行结果</span>
            <el-tag v-if="executeTime" type="info" size="small">
              耗时: {{ executeTime }}ms
            </el-tag>
          </div>
        </template>
        
        <el-table 
          v-if="tableData.length > 0" 
          :data="tableData" 
          border 
          stripe
          max-height="400"
        >
          <el-table-column 
            v-for="col in tableColumns" 
            :key="col" 
            :prop="col" 
            :label="col"
            show-overflow-tooltip
          />
        </el-table>
        
        <div v-else-if="errorMessage" class="error-message">
          <el-alert type="error" :title="errorMessage" :closable="false" />
        </div>
        
        <el-empty v-else description="执行SQL后将在此显示结果" />
        
        <div v-if="totalCount > 0" class="pagination-wrapper">
          <span class="total-count">共 {{ totalCount }} 条</span>
        </div>
      </el-card>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { testApi } from '@/api/test'

const loading = ref(false)
const selectedDatasource = ref('mysql')
const sqlContent = ref('')
const executeTime = ref(0)
const errorMessage = ref('')
const tableColumns = ref([])
const tableData = ref([])
const totalCount = ref(0)
const historyList = ref([])

const executeSql = async () => {
  if (!sqlContent.value.trim()) {
    ElMessage.warning('请输入SQL语句')
    return
  }
  
  loading.value = true
  errorMessage.value = ''
  const startTime = Date.now()
  
  try {
    const res = await testApi.execute({
      datasource: selectedDatasource.value,
      sql: sqlContent.value
    })
    
    executeTime.value = Date.now() - startTime
    
    if (res.data) {
      if (Array.isArray(res.data)) {
        tableData.value = res.data
        if (res.data.length > 0) {
          tableColumns.value = Object.keys(res.data[0])
          totalCount.value = res.data.length
        }
      } else if (res.data.records) {
        tableData.value = res.data.records
        tableColumns.value = res.data.columns || []
        totalCount.value = res.data.total || res.data.records.length
      } else {
        tableData.value = []
        tableColumns.value = []
        totalCount.value = 0
      }
    }
    
    historyList.value.unshift({
      sql: sqlContent.value,
      success: true,
      time: new Date().toLocaleTimeString()
    })
    
    if (historyList.value.length > 20) {
      historyList.value.pop()
    }
    
    ElMessage.success('执行成功')
  } catch (error) {
    executeTime.value = Date.now() - startTime
    errorMessage.value = error.message || 'SQL执行失败'
    tableData.value = []
    tableColumns.value = []
    totalCount.value = 0
    
    historyList.value.unshift({
      sql: sqlContent.value,
      success: false,
      time: new Date().toLocaleTimeString()
    })
    
    ElMessage.error('执行失败')
  } finally {
    loading.value = false
  }
}

const formatSql = () => {
  let sql = sqlContent.value.trim()
  sql = sql.replace(/\s+/g, ' ')
  sql = sql.replace(/,\s/g, ',\n  ')
  sql = sql.replace(/FROM\s+/gi, 'FROM\n  ')
  sql = sql.replace(/WHERE\s+/gi, 'WHERE\n  ')
  sql = sql.replace(/AND\s+/gi, 'AND\n  ')
  sql = sql.replace(/OR\s+/gi, 'OR\n  ')
  sql = sql.replace(/ORDER BY\s+/gi, 'ORDER BY\n  ')
  sql = sql.replace(/GROUP BY\s+/gi, 'GROUP BY\n  ')
  sql = sql.replace(/HAVING\s+/gi, 'HAVING\n  ')
  sql = sql.replace(/LIMIT\s+/gi, 'LIMIT\n  ')
  sql = sql.replace(/JOIN\s+/gi, 'JOIN\n  ')
  sql = sql.replace(/LEFT JOIN\s+/gi, 'LEFT JOIN\n  ')
  sql = sql.replace(/RIGHT JOIN\s+/gi, 'RIGHT JOIN\n  ')
  sql = sql.replace(/INNER JOIN\s+/gi, 'INNER JOIN\n  ')
  sqlContent.value = sql
}

const clearSql = () => {
  sqlContent.value = ''
  tableData.value = []
  tableColumns.value = []
  errorMessage.value = ''
  executeTime.value = 0
  totalCount.value = 0
}

const loadHistory = (item) => {
  sqlContent.value = item.sql
}
</script>

<style scoped>
.sql-test-container {
  padding: 0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.result-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.sql-editor :deep(.el-textarea__inner) {
  font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
  font-size: 14px;
}

.editor-actions {
  margin-top: 15px;
  display: flex;
  gap: 10px;
}

.history-list {
  max-height: 300px;
  overflow-y: auto;
}

.history-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 8px;
  cursor: pointer;
  border-bottom: 1px solid #f0f0f0;
}

.history-item:hover {
  background: #f5f7fa;
}

.history-sql {
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  font-size: 13px;
}

.history-time {
  font-size: 12px;
  color: #999;
}

.error-message {
  padding: 20px;
}

.pagination-wrapper {
  margin-top: 15px;
  display: flex;
  justify-content: flex-end;
}

.total-count {
  color: #666;
  font-size: 14px;
}
</style>

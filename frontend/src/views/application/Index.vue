<template>
  <div class="application">
    <h2>应用管理</h2>
    
    <el-card>
      <div class="toolbar">
        <div class="search-box">
          <el-input
            v-model="searchKey"
            placeholder="搜索应用名称或描述..."
            style="width: 300px;"
            clearable
            @clear="handleSearch"
            @keyup.enter="handleSearch"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
          <el-select
            v-model="filterStatus"
            placeholder="状态筛选"
            style="width: 150px; margin-left: 10px;"
            clearable
          >
            <el-option label="运行中" value="running" />
            <el-option label="已停止" value="stopped" />
          </el-select>
        </div>
        <div class="action-box">
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon> 新增应用
          </el-button>
        </div>
      </div>

      <el-table
        v-loading="loading"
        :data="pagedData"
        style="width: 100%; margin-top: 20px;"
        :row-class-name="tableRowClassName"
      >
        <el-table-column prop="name" label="应用名称" min-width="150">
          <template #default="{ row }">
            <div class="app-name-cell">
              <el-icon :size="24" :color="row.color">
                <component :is="getIcon(row.icon)" />
              </el-icon>
              <span>{{ row.name }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="code" label="应用编码" width="150" />
        <el-table-column prop="description" label="描述" min-width="200" show-overflow-tooltip />
        <el-table-column prop="version" label="版本" width="100" align="center" />
        <el-table-column prop="instances" label="实例数" width="100" align="center" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 'running' ? 'success' : 'info'" size="small">
              {{ row.status === 'running' ? '运行中' : '已停止' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right" align="center">
          <template #default="{ row }">
            <el-button size="small" type="primary" link @click="handleManage(row)">管理</el-button>
            <el-button size="small" type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button size="small" type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
        
        <template #empty>
          <el-empty description="暂无应用数据">
            <el-button type="primary" @click="handleAdd">新增应用</el-button>
          </el-empty>
        </template>
      </el-table>

      <div class="pagination-wrapper" v-if="filteredData.length > 0">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="filteredData.length"
          layout="total, sizes, prev, pager, next, jumper"
          background
        />
      </div>
    </el-card>

    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      @close="handleDialogClose"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="100px"
      >
        <el-form-item label="应用名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入应用名称" />
        </el-form-item>
        <el-form-item label="应用编码" prop="code">
          <el-input v-model="formData.code" placeholder="请输入应用编码" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="应用描述" prop="description">
          <el-input
            v-model="formData.description"
            type="textarea"
            :rows="3"
            placeholder="请输入应用描述"
          />
        </el-form-item>
        <el-form-item label="版本号" prop="version">
          <el-input v-model="formData.version" placeholder="如: v1.0.0" />
        </el-form-item>
        <el-form-item label="实例数" prop="instances">
          <el-input-number v-model="formData.instances" :min="1" :max="99" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="formData.status">
            <el-radio value="running">运行中</el-radio>
            <el-radio value="stopped">已停止</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="主题色" prop="color">
          <el-color-picker v-model="formData.color" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitting">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Plus, Box, Connection, Monitor, DataBoard, Setting, ShoppingCart, Document } from '@element-plus/icons-vue'

const iconMap = {
  Box,
  Connection,
  Monitor,
  DataBoard,
  Setting,
  ShoppingCart,
  Document
}

const getIcon = (iconName) => {
  return iconMap[iconName] || Box
}

const loading = ref(false)
const searchKey = ref('')
const filterStatus = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const dialogVisible = ref(false)
const submitting = ref(false)
const isEdit = ref(false)
const formRef = ref(null)

const formData = reactive({
  id: null,
  name: '',
  code: '',
  description: '',
  version: 'v1.0.0',
  instances: 1,
  status: 'stopped',
  color: '#409eff',
  icon: 'Box'
})

const formRules = {
  name: [{ required: true, message: '请输入应用名称', trigger: 'blur' }],
  code: [
    { required: true, message: '请输入应用编码', trigger: 'blur' },
    { pattern: /^[a-z][a-z0-9_-]*$/, message: '编码格式：以小写字母开头，仅包含小写字母、数字、下划线和连字符', trigger: 'blur' }
  ],
  description: [{ max: 200, message: '描述不超过200字符', trigger: 'blur' }]
}

const apps = ref([
  { id: 1, name: '用户中心', code: 'user-center', description: '负责用户注册、登录、认证等功能', icon: 'Box', color: '#409eff', status: 'running', instances: 3, version: 'v2.1.0' },
  { id: 2, name: '订单系统', code: 'order-system', description: '处理订单创建、支付、物流等业务', icon: 'ShoppingCart', color: '#67c23a', status: 'running', instances: 5, version: 'v1.8.2' },
  { id: 3, name: '监控系统', code: 'monitor-system', description: '实时监控系统运行状态和性能指标', icon: 'Monitor', color: '#e6a23c', status: 'running', instances: 2, version: 'v3.0.1' },
  { id: 4, name: '数据平台', code: 'data-platform', description: '数据采集、分析和可视化展示', icon: 'DataBoard', color: '#f56c6c', status: 'running', instances: 4, version: 'v2.5.0' },
  { id: 5, name: '配置中心', code: 'config-center', description: '统一管理系统配置和参数', icon: 'Setting', color: '#909399', status: 'stopped', instances: 1, version: 'v1.2.0' },
  { id: 6, name: '网关服务', code: 'api-gateway', description: 'API网关，统一入口和流量控制', icon: 'Connection', color: '#9c27b0', status: 'running', instances: 2, version: 'v4.1.0' },
  { id: 7, name: '文档中心', code: 'doc-center', description: '系统文档和API文档管理', icon: 'Document', color: '#00bcd4', status: 'running', instances: 2, version: 'v1.0.0' },
  { id: 8, name: '消息中心', code: 'msg-center', description: '消息推送和通知服务', icon: 'Connection', color: '#ff5722', status: 'stopped', instances: 1, version: 'v0.9.0' }
])

const filteredData = computed(() => {
  let result = apps.value
  
  if (searchKey.value) {
    const keyword = searchKey.value.toLowerCase()
    result = result.filter(app => 
      app.name.toLowerCase().includes(keyword) || 
      app.code.toLowerCase().includes(keyword) ||
      app.description.toLowerCase().includes(keyword)
    )
  }
  
  if (filterStatus.value) {
    result = result.filter(app => app.status === filterStatus.value)
  }
  
  return result
})

const pagedData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return filteredData.value.slice(start, end)
})

const tableRowClassName = ({ row }) => {
  return row.status === 'running' ? 'running-row' : 'stopped-row'
}

const handleSearch = () => {
  currentPage.value = 1
}

const handleAdd = () => {
  isEdit.value = false
  Object.assign(formData, {
    id: null,
    name: '',
    code: '',
    description: '',
    version: 'v1.0.0',
    instances: 1,
    status: 'stopped',
    color: '#409eff',
    icon: 'Box'
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  Object.assign(formData, {
    id: row.id,
    name: row.name,
    code: row.code,
    description: row.description,
    version: row.version,
    instances: row.instances,
    status: row.status,
    color: row.color,
    icon: row.icon
  })
  dialogVisible.value = true
}

const handleManage = (row) => {
  ElMessage.info(`管理 ${row.name}`)
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除应用 "${row.name}" 吗？删除后将无法恢复。`,
      '删除确认',
      { type: 'warning', confirmButtonText: '删除', cancelButtonText: '取消' }
    )
    const index = apps.value.findIndex(app => app.id === row.id)
    if (index > -1) {
      apps.value.splice(index, 1)
      ElMessage.success('删除成功')
    }
  } catch {
    // 用户取消
  }
}

const handleDialogClose = () => {
  formRef.value?.resetFields()
}

const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    submitting.value = true
    
    await new Promise(resolve => setTimeout(resolve, 500))
    
    if (isEdit.value) {
      const index = apps.value.findIndex(app => app.id === formData.id)
      if (index > -1) {
        apps.value[index] = { ...formData }
      }
      ElMessage.success('更新成功')
    } else {
      const newApp = {
        ...formData,
        id: Date.now(),
        icon: formData.icon
      }
      apps.value.unshift(newApp)
      ElMessage.success('创建成功')
    }
    
    dialogVisible.value = false
    handleSearch()
  } catch (error) {
    if (error !== false) {
      console.error('表单验证失败', error)
    }
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.application h2 {
  margin-bottom: 20px;
  color: #333;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-box {
  display: flex;
  align-items: center;
}

.pagination-wrapper {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

.app-name-cell {
  display: flex;
  align-items: center;
  gap: 10px;
}

:deep(.el-table .running-row) {
  --el-table-tr-bg-color: var(--el-color-success-lighter);
}

:deep(.el-table .stopped-row) {
  --el-table-tr-bg-color: var(--el-color-info-lighter);
}
</style>

<template>
  <div class="integration">
    <h2>系统集成</h2>
    
    <el-row :gutter="20">
      <el-col :span="16">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>第三方平台</span>
              <el-button type="primary" size="small" @click="handleAdd">
                <el-icon><Plus /></el-icon> 添加平台
              </el-button>
            </div>
          </template>
          <el-table
            v-loading="loading"
            :data="platforms"
            style="width: 100%"
          >
            <el-table-column prop="name" label="平台名称" min-width="150">
              <template #default="{ row }">
                <div class="platform-name">
                  <el-icon :size="20" :color="row.color">
                    <component :is="getIcon(row.icon)" />
                  </el-icon>
                  <span>{{ row.name }}</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="type" label="类型" width="120">
              <template #default="{ row }">
                <el-tag size="small">{{ row.type }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="url" label="平台地址" min-width="200" show-overflow-tooltip />
            <el-table-column prop="status" label="状态" width="100" align="center">
              <template #default="{ row }">
                <el-tag :type="row.status === 'connected' ? 'success' : 'info'" size="small">
                  {{ row.status === 'connected' ? '已连接' : '未连接' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="200" fixed="right" align="center">
              <template #default="{ row }">
                <el-button size="small" type="primary" link @click="handleView(row)">查看</el-button>
                <el-button size="small" type="primary" link @click="handleEdit(row)">编辑</el-button>
                <el-button size="small" type="danger" link @click="handleDelete(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      
      <el-col :span="8">
        <el-card>
          <template #header>
            <span>平台统计</span>
          </template>
          <div class="stats-grid">
            <div class="stat-item">
              <span class="stat-label">平台总数</span>
              <span class="stat-value">{{ platforms.length }}</span>
            </div>
            <div class="stat-item">
              <span class="stat-label">已连接</span>
              <span class="stat-value success">{{ connectedCount }}</span>
            </div>
            <div class="stat-item">
              <span class="stat-label">未连接</span>
              <span class="stat-value info">{{ disconnectedCount }}</span>
            </div>
            <div class="stat-item">
              <span class="stat-label">访问次数</span>
              <span class="stat-value">156</span>
            </div>
          </div>
        </el-card>

        <el-card style="margin-top: 20px;">
          <template #header>
            <span>快速操作</span>
          </template>
          <div class="quick-actions">
            <el-button type="primary" plain style="width: 100%; margin-bottom: 10px;" @click="handleGenerateToken">
              <el-icon><Key /></el-icon> 生成 SSO Token
            </el-button>
            <el-button type="success" plain style="width: 100%;" @click="handleRefreshAll">
              <el-icon><Refresh /></el-icon> 刷新所有平台
            </el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;" v-if="selectedPlatform">
      <el-col :span="24">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>
                <el-icon><Monitor /></el-icon>
                {{ selectedPlatform.name }} - {{ selectedPlatform.url }}
              </span>
              <div class="header-actions">
                <el-tag type="info" size="small" v-if="ssoToken">
                  <el-icon><Key /></el-icon> SSO Token: {{ ssoToken.substring(0, 20) }}...
                </el-tag>
                <el-button size="small" @click="handleRefresh">
                  <el-icon><Refresh /></el-icon> 刷新
                </el-button>
              </div>
            </div>
          </template>
          <IframeContainer
            ref="iframeRef"
            :src="iframeUrl"
            :loading="iframeLoading"
            :error="iframeError"
            @load="handleIframeLoad"
            @error="handleIframeError"
            @refresh="handleRefresh"
          />
        </el-card>
      </el-col>
    </el-row>

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
        <el-form-item label="平台名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入平台名称" />
        </el-form-item>
        <el-form-item label="平台编码" prop="code">
          <el-input v-model="formData.code" placeholder="请输入唯一编码" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="平台类型" prop="type">
          <el-select v-model="formData.type" placeholder="请选择类型" style="width: 100%;">
            <el-option label="办公协同" value="办公" />
            <el-option label="云服务" value="云服务" />
            <el-option label="存储" value="存储" />
            <el-option label="通知" value="通知" />
            <el-option label="数据分析" value="数据分析" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="平台地址" prop="url">
          <el-input v-model="formData.url" placeholder="https://example.com" />
        </el-form-item>
        <el-form-item label="连接状态" prop="status">
          <el-radio-group v-model="formData.status">
            <el-radio value="connected">已连接</el-radio>
            <el-radio value="disconnected">未连接</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="主题色" prop="color">
          <el-color-picker v-model="formData.color" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="formData.remark" type="textarea" :rows="3" placeholder="请输入备注信息" />
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
import { Plus, Refresh, Key, Monitor, Link, Document, Folder, PieChart, Message } from '@element-plus/icons-vue'
import IframeContainer from '@/components/common/IframeContainer.vue'
import request from '@/api'

const iconMap = {
  Link,
  Document,
  Folder,
  PieChart,
  Message
}

const getIcon = (iconName) => {
  return iconMap[iconName] || Link
}

const loading = ref(false)
const platforms = ref([
  { id: 1, name: '钉钉办公平台', code: 'dingtalk', type: '办公', url: 'https://oapi.dingtalk.com', status: 'connected', color: '#1677ff', icon: 'Link', remark: '钉钉办公系统' },
  { id: 2, name: '企业微信', code: 'wecom', type: '办公', url: 'https://work.weixin.qq.com', status: 'connected', color: '#07c160', icon: 'Message', remark: '' },
  { id: 3, name: '飞书', code: 'feishu', type: '办公', url: 'https://www.feishu.cn', status: 'connected', color: '#4263eb', icon: 'Document', remark: '' },
  { id: 4, name: '阿里云 OSS', code: 'aliyun-oss', type: '存储', url: 'https://oss.console.aliyun.com', status: 'connected', color: '#ff6a00', icon: 'Folder', remark: '' },
  { id: 5, name: '数据分析平台', code: 'data-analysis', type: '数据分析', url: 'https://data.example.com', status: 'disconnected', color: '#722ed1', icon: 'PieChart', remark: '待配置' }
])

const dialogVisible = ref(false)
const submitting = ref(false)
const isEdit = ref(false)
const formRef = ref(null)
const selectedPlatform = ref(null)
const ssoToken = ref('')
const iframeLoading = ref(false)
const iframeError = ref('')
const iframeRef = ref(null)

const formData = reactive({
  id: null,
  name: '',
  code: '',
  type: '办公',
  url: '',
  status: 'disconnected',
  color: '#409eff',
  icon: 'Link',
  remark: ''
})

const formRules = {
  name: [{ required: true, message: '请输入平台名称', trigger: 'blur' }],
  code: [
    { required: true, message: '请输入平台编码', trigger: 'blur' },
    { pattern: /^[a-z][a-z0-9_-]*$/, message: '编码格式：以小写字母开头', trigger: 'blur' }
  ],
  type: [{ required: true, message: '请选择平台类型', trigger: 'change' }],
  url: [
    { required: true, message: '请输入平台地址', trigger: 'blur' },
    { type: 'url', message: '请输入有效的URL地址', trigger: 'blur' }
  ]
}

const connectedCount = computed(() => platforms.value.filter(p => p.status === 'connected').length)
const disconnectedCount = computed(() => platforms.value.filter(p => p.status === 'disconnected').length)

const iframeUrl = computed(() => {
  if (!selectedPlatform.value) return ''
  if (ssoToken.value) {
    const separator = selectedPlatform.value.url.includes('?') ? '&' : '?'
    return `${selectedPlatform.value.url}${separator}token=${ssoToken.value}&userId=${localStorage.getItem('userId') || ''}`
  }
  return selectedPlatform.value.url
})

const handleAdd = () => {
  isEdit.value = false
  Object.assign(formData, {
    id: null,
    name: '',
    code: '',
    type: '办公',
    url: '',
    status: 'disconnected',
    color: '#409eff',
    icon: 'Link',
    remark: ''
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  Object.assign(formData, { ...row })
  dialogVisible.value = true
}

const handleView = async (row) => {
  selectedPlatform.value = row
  
  try {
    iframeLoading.value = true
    iframeError.value = ''
    
    const userId = localStorage.getItem('userId') || 'demo'
    const res = await request.get(`/integrate/platforms/${row.id}/sso-token?userId=${userId}`)
    
    if (res.code === 200 && res.data) {
      ssoToken.value = res.data
    } else {
      ssoToken.value = 'demo-token-' + Date.now()
    }
  } catch (error) {
    console.warn('SSO Token获取失败，使用演示模式:', error)
    ssoToken.value = 'demo-token-' + Date.now()
  } finally {
    iframeLoading.value = false
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确定要删除平台 "${row.name}" 吗？`, '删除确认', {
      type: 'warning'
    })
    const index = platforms.value.findIndex(p => p.id === row.id)
    if (index > -1) {
      platforms.value.splice(index, 1)
      if (selectedPlatform.value?.id === row.id) {
        selectedPlatform.value = null
        ssoToken.value = ''
      }
    }
    ElMessage.success('删除成功')
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
      const index = platforms.value.findIndex(p => p.id === formData.id)
      if (index > -1) {
        platforms.value[index] = { ...formData }
      }
      ElMessage.success('更新成功')
    } else {
      platforms.value.unshift({
        ...formData,
        id: Date.now()
      })
      ElMessage.success('创建成功')
    }
    
    dialogVisible.value = false
  } catch (error) {
    if (error !== false) {
      console.error('表单验证失败', error)
    }
  } finally {
    submitting.value = false
  }
}

const handleIframeLoad = () => {
  iframeLoading.value = false
  iframeError.value = ''
}

const handleIframeError = (error) => {
  iframeLoading.value = false
  iframeError.value = error || '加载失败'
}

const handleRefresh = () => {
  iframeLoading.value = true
  iframeError.value = ''
  if (iframeRef.value) {
    iframeRef.value.refresh()
  }
}

const handleGenerateToken = async () => {
  try {
    const userId = localStorage.getItem('userId') || 'demo'
    const res = await request.get(`/integrate/sso-token?userId=${userId}`)
    if (res.code === 200 && res.data) {
      ssoToken.value = res.data
      ElMessage.success('SSO Token 已生成')
    }
  } catch {
    ssoToken.value = 'demo-token-' + Date.now()
    ElMessage.warning('使用演示Token')
  }
}

const handleRefreshAll = () => {
  platforms.value.forEach(p => {
    p.status = Math.random() > 0.3 ? 'connected' : 'disconnected'
  })
  ElMessage.success('已刷新所有平台状态')
}
</script>

<style scoped>
.integration h2 {
  margin-bottom: 20px;
  color: #333;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.platform-name {
  display: flex;
  align-items: center;
  gap: 10px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

.stat-item {
  text-align: center;
  padding: 15px;
  background: #f9f9f9;
  border-radius: 8px;
}

.stat-label {
  display: block;
  color: #999;
  font-size: 13px;
  margin-bottom: 8px;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #333;
}

.stat-value.success {
  color: #67c23a;
}

.stat-value.info {
  color: #909399;
}

.quick-actions {
  display: flex;
  flex-direction: column;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 10px;
}
</style>

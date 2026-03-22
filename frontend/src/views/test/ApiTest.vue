<template>
  <div class="api-test-container">
    <el-card class="main-card">
      <template #header>
        <div class="card-header">
          <span>API 测试工具</span>
        </div>
      </template>
      
      <el-row :gutter="20">
        <el-col :span="14">
          <el-card shadow="never" class="request-card">
            <template #header>
              <span>请求配置</span>
            </template>
            
            <el-form :model="requestForm" label-width="100px">
              <el-form-item label="请求方法">
                <el-select v-model="requestForm.method" style="width: 120px">
                  <el-option label="GET" value="GET" />
                  <el-option label="POST" value="POST" />
                  <el-option label="PUT" value="PUT" />
                  <el-option label="DELETE" value="DELETE" />
                </el-select>
              </el-form-item>
              
              <el-form-item label="请求URL">
                <el-input 
                  v-model="requestForm.url" 
                  placeholder="请输入请求URL"
                  clearable
                />
              </el-form-item>
              
              <el-form-item label="请求头">
                <el-input
                  v-model="requestForm.headers"
                  type="textarea"
                  :rows="3"
                  placeholder="JSON格式，如：{&quot;Content-Type&quot;: &quot;application/json&quot;}"
                />
              </el-form-item>
              
              <el-form-item label="请求参数">
                <el-input
                  v-model="requestForm.params"
                  type="textarea"
                  :rows="4"
                  placeholder="JSON格式请求参数"
                />
              </el-form-item>
              
              <el-form-item>
                <el-button type="primary" @click="sendRequest" :loading="loading">
                  发送请求
                </el-button>
                <el-button @click="clearForm">清空</el-button>
              </el-form-item>
            </el-form>
          </el-card>
        </el-col>
        
        <el-col :span="10">
          <el-card shadow="never" class="history-card">
            <template #header>
              <span>历史记录</span>
            </template>
            
            <div class="history-list">
              <div 
                v-for="(item, index) in historyList" 
                :key="index"
                class="history-item"
                @click="loadHistory(item)"
              >
                <el-tag :type="getMethodType(item.method)" size="small">
                  {{ item.method }}
                </el-tag>
                <span class="history-url">{{ item.url }}</span>
                <span class="history-time">{{ item.time }}</span>
              </div>
              <el-empty v-if="historyList.length === 0" description="暂无历史记录" />
            </div>
          </el-card>
        </el-col>
      </el-row>
      
      <el-card shadow="never" class="response-card" style="margin-top: 20px">
        <template #header>
          <span>响应结果</span>
          <el-tag v-if="responseTime" type="info" size="small">
            耗时: {{ responseTime }}ms
          </el-tag>
        </template>
        
        <el-input
          v-model="responseData"
          type="textarea"
          :rows="10"
          readonly
          placeholder="响应结果将显示在这里"
        />
      </el-card>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'

const loading = ref(false)
const responseTime = ref(0)
const responseData = ref('')
const historyList = ref([])

const requestForm = reactive({
  method: 'GET',
  url: '',
  headers: '{\n  "Content-Type": "application/json"\n}',
  params: ''
})

const getMethodType = (method) => {
  const types = {
    GET: '',
    POST: 'success',
    PUT: 'warning',
    DELETE: 'danger'
  }
  return types[method] || ''
}

const sendRequest = async () => {
  if (!requestForm.url) {
    ElMessage.warning('请输入请求URL')
    return
  }
  
  loading.value = true
  const startTime = Date.now()
  
  try {
    let headers = {}
    try {
      headers = JSON.parse(requestForm.headers || '{}')
    } catch (e) {
      headers = {}
    }
    
    let data = null
    if (requestForm.method !== 'GET') {
      try {
        data = JSON.parse(requestForm.params || '{}')
      } catch (e) {
        data = requestForm.params
      }
    }
    
    const config = {
      method: requestForm.method,
      url: requestForm.url,
      headers,
      timeout: 30000
    }
    
    if (requestForm.method === 'GET') {
      config.params = data
    } else {
      config.data = data
    }
    
    const res = await axios(config)
    responseTime.value = Date.now() - startTime
    responseData.value = JSON.stringify(res.data, null, 2)
    
    historyList.value.unshift({
      method: requestForm.method,
      url: requestForm.url,
      time: new Date().toLocaleTimeString()
    })
    
    if (historyList.value.length > 20) {
      historyList.value.pop()
    }
    
    ElMessage.success('请求成功')
  } catch (error) {
    responseTime.value = Date.now() - startTime
    responseData.value = JSON.stringify({
      error: true,
      message: error.message,
      status: error.response?.status,
      data: error.response?.data
    }, null, 2)
    ElMessage.error('请求失败')
  } finally {
    loading.value = false
  }
}

const clearForm = () => {
  requestForm.url = ''
  requestForm.params = ''
  responseData.value = ''
  responseTime.value = 0
}

const loadHistory = (item) => {
  requestForm.method = item.method
  requestForm.url = item.url
}
</script>

<style scoped>
.api-test-container {
  padding: 0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
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

.history-url {
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
</style>

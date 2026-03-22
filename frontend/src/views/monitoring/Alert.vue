<template>
  <div class="alert-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <el-tabs v-model="activeTab">
            <el-tab-pane label="告警规则" name="rule"></el-tab-pane>
            <el-tab-pane label="接收人管理" name="receiver"></el-tab-pane>
            <el-tab-pane label="告警历史" name="history"></el-tab-pane>
          </el-tabs>
          <el-button v-if="activeTab !== 'history'" type="primary" @click="handleCreate">
            新增{{ activeTab === 'rule' ? '规则' : '接收人' }}
          </el-button>
        </div>
      </template>

      <div v-if="activeTab === 'rule'">
        <el-table :data="ruleList" border stripe v-loading="loading">
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column prop="ruleName" label="规则名称" />
          <el-table-column prop="ruleCode" label="规则编码" />
          <el-table-column prop="metricType" label="指标类型" />
          <el-table-column prop="conditionType" label="条件">
            <template #default="{ row }">
              {{ getConditionLabel(row.conditionType) }} {{ row.thresholdValue }}
            </template>
          </el-table-column>
          <el-table-column prop="timeWindow" label="时间窗口(秒)" />
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="row.status === 1 ? 'success' : 'info'" size="small">
                {{ row.status === 1 ? '启用' : '禁用' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="150">
            <template #default="{ row }">
              <el-button link type="primary" @click="handleEditRule(row)">编辑</el-button>
              <el-button link type="danger" @click="handleDeleteRule(row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <div v-else-if="activeTab === 'receiver'">
        <el-table :data="receiverList" border stripe v-loading="loading">
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column prop="receiverName" label="接收人名称" />
          <el-table-column prop="receiverType" label="类型" />
          <el-table-column prop="notifyChannels" label="通知渠道">
            <template #default="{ row }">
              <el-tag v-for="ch in JSON.parse(row.notifyChannels || '[]')" :key="ch" size="small" style="margin-right:5px">
                {{ ch }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="row.status === 1 ? 'success' : 'info'" size="small">
                {{ row.status === 1 ? '启用' : '禁用' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="150">
            <template #default="{ row }">
              <el-button link type="primary" @click="handleEditReceiver(row)">编辑</el-button>
              <el-button link type="danger" @click="handleDeleteReceiver(row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <div v-else>
        <el-table :data="historyList" border stripe v-loading="loading">
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column prop="ruleName" label="规则名称" />
          <el-table-column prop="alertLevel" label="级别">
            <template #default="{ row }">
              <el-tag :type="getLevelType(row.alertLevel)" size="small">{{ row.alertLevel }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="metricValue" label="指标值" />
          <el-table-column prop="alertMessage" label="消息" show-overflow-tooltip />
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="getStatusType(row.status)" size="small">{{ row.status }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createdAt" label="触发时间" />
          <el-table-column label="操作" width="150">
            <template #default="{ row }">
              <el-button v-if="row.status === 'PENDING'" link type="success" @click="handleAck(row)">确认</el-button>
              <el-button v-if="row.status === 'ACKED'" link type="primary" @click="handleClose(row)">关闭</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form ref="formRef" :model="form" label-width="100px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item v-if="activeTab === 'rule'" label="规则编码" prop="code">
          <el-input v-model="form.code" placeholder="请输入编码" />
        </el-form-item>
        <el-form-item v-if="activeTab === 'rule'" label="指标类型">
          <el-select v-model="form.metricType" placeholder="选择指标类型">
            <el-option label="CPU使用率" value="SYSTEM_CPU" />
            <el-option label="内存使用率" value="SYSTEM_MEMORY" />
            <el-option label="磁盘使用率" value="SYSTEM_DISK" />
          </el-select>
        </el-form-item>
        <el-form-item v-if="activeTab === 'rule'" label="条件">
          <el-select v-model="form.conditionType" placeholder="选择条件">
            <el-option label="大于" value="GT" />
            <el-option label="小于" value="LT" />
            <el-option label="等于" value="EQ" />
            <el-option label="大于等于" value="GE" />
            <el-option label="小于等于" value="LE" />
          </el-select>
        </el-form-item>
        <el-form-item v-if="activeTab === 'rule'" label="阈值">
          <el-input-number v-model="form.thresholdValue" :min="0" :max="100" />
        </el-form-item>
        <el-form-item v-if="activeTab === 'rule'" label="时间窗口">
          <el-input-number v-model="form.timeWindow" :min="60" :step="60" />
          <span class="ml-2">秒</span>
        </el-form-item>
        <el-form-item v-if="activeTab === 'receiver'" label="通知渠道">
          <el-checkbox-group v-model="form.notifyChannels">
            <el-checkbox label="EMAIL">邮件</el-checkbox>
            <el-checkbox label="SMS">短信</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { alertApi } from '@/api/alert'

const activeTab = ref('rule')
const loading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)

const ruleList = ref([])
const receiverList = ref([])
const historyList = ref([])

const form = reactive({
  name: '', code: '', metricType: '', conditionType: 'GT',
  thresholdValue: 80, timeWindow: 300, notifyChannels: [], status: 1
})

const dialogTitle = computed(() => {
  if (activeTab.value === 'rule') return isEdit.value ? '编辑规则' : '新增规则'
  return isEdit.value ? '编辑接收人' : '新增接收人'
})

const getConditionLabel = (type) => ({ GT: '>', LT: '<', EQ: '=', GE: '>=', LE: '<=' })[type] || type
const getLevelType = (level) => ({ WARNING: 'warning', ERROR: 'danger', CRITICAL: 'danger' })[level] || ''
const getStatusType = (status) => ({ PENDING: 'warning', SENT: 'info', ACKED: 'success', CLOSED: 'info' })[status] || ''

const fetchRules = async () => {
  try {
    const res = await alertApi.listRules()
    ruleList.value = res.data || []
  } catch (e) { ElMessage.error('加载规则失败') }
}

const fetchReceivers = async () => {
  try {
    const res = await alertApi.listReceivers()
    receiverList.value = res.data || []
  } catch (e) { ElMessage.error('加载接收人失败') }
}

const fetchHistory = async () => {
  try {
    const res = await alertApi.getHistory()
    historyList.value = res.data || []
  } catch (e) { ElMessage.error('加载历史失败') }
}

const handleCreate = () => {
  isEdit.value = false
  Object.assign(form, { name: '', code: '', metricType: '', conditionType: 'GT', thresholdValue: 80, timeWindow: 300, notifyChannels: [], status: 1 })
  dialogVisible.value = true
}

const handleEditRule = (row) => {
  isEdit.value = true
  Object.assign(form, { name: row.ruleName, code: row.ruleCode, metricType: row.metricType, conditionType: row.conditionType, thresholdValue: parseInt(row.thresholdValue), timeWindow: row.timeWindow, status: row.status })
  dialogVisible.value = true
}

const handleEditReceiver = (row) => {
  isEdit.value = true
  Object.assign(form, { name: row.receiverName, notifyChannels: JSON.parse(row.notifyChannels || '[]'), status: row.status })
  dialogVisible.value = true
}

const handleSubmit = async () => {
  try {
    if (activeTab.value === 'rule') {
      if (isEdit.value) await alertApi.updateRule(form)
      else await alertApi.createRule(form)
    } else {
      if (isEdit.value) await alertApi.updateReceiver(form)
      else await alertApi.createReceiver(form)
    }
    ElMessage.success('操作成功')
    dialogVisible.value = false
    activeTab.value === 'rule' ? fetchRules() : fetchReceivers()
  } catch (e) { ElMessage.error('操作失败') }
}

const handleDeleteRule = async (row) => {
  await ElMessageBox.confirm(`删除规则 "${row.ruleName}"?`)
  await alertApi.deleteRule(row.id)
  ElMessage.success('删除成功')
  fetchRules()
}

const handleDeleteReceiver = async (row) => {
  await ElMessageBox.confirm(`删除接收人 "${row.receiverName}"?`)
  await alertApi.deleteReceiver(row.id)
  ElMessage.success('删除成功')
  fetchReceivers()
}

const handleAck = async (row) => {
  await alertApi.acknowledge(row.id, 1)
  ElMessage.success('已确认')
  fetchHistory()
}

const handleClose = async (row) => {
  await alertApi.close(row.id)
  ElMessage.success('已关闭')
  fetchHistory()
}

onMounted(() => {
  fetchRules()
  fetchReceivers()
  fetchHistory()
})
</script>

<style scoped>
.alert-page { padding: 0; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
.ml-2 { margin-left: 8px; }
</style>

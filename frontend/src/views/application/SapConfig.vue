<template>
  <div class="sap-config-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <el-tabs v-model="activeTab">
            <el-tab-pane label="RFC配置" name="rfc"></el-tab-pane>
            <el-tab-pane label="API配置" name="api"></el-tab-pane>
            <el-tab-pane label="WebService配置" name="webservice"></el-tab-pane>
            <el-tab-pane label="HANA连接" name="hana"></el-tab-pane>
          </el-tabs>
          <el-button type="primary" @click="handleCreate">新增</el-button>
        </div>
      </template>

      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="configName" label="配置名称" />
        <el-table-column prop="configCode" label="配置编码" />
        <el-table-column v-if="activeTab === 'hana'" prop="host" label="主机" />
        <el-table-column v-if="activeTab === 'hana'" prop="port" label="端口" />
        <el-table-column v-if="activeTab === 'hana'" prop="databaseName" label="数据库" />
        <el-table-column v-if="activeTab === 'api'" prop="baseUrl" label="API地址" show-overflow-tooltip />
        <el-table-column v-if="activeTab === 'webservice'" prop="wsdlUrl" label="WSDL地址" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'" size="small">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button v-if="activeTab === 'hana'" link type="success" @click="handleTest(row)">测试</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form ref="formRef" :model="form" label-width="120px">
        <el-form-item label="配置名称" prop="configName">
          <el-input v-model="form.configName" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item label="配置编码" prop="configCode">
          <el-input v-model="form.configCode" placeholder="请输入编码" />
        </el-form-item>
        
        <template v-if="activeTab === 'hana'">
          <el-form-item label="主机">
            <el-input v-model="form.host" placeholder="如: localhost" />
          </el-form-item>
          <el-form-item label="端口">
            <el-input-number v-model="form.port" :min="1" :max="65535" />
          </el-form-item>
          <el-form-item label="数据库名">
            <el-input v-model="form.databaseName" placeholder="如: HXE" />
          </el-form-item>
          <el-form-item label="用户名">
            <el-input v-model="form.username" placeholder="数据库用户名" />
          </el-form-item>
          <el-form-item label="密码">
            <el-input v-model="form.password" type="password" placeholder="数据库密码" show-password />
          </el-form-item>
        </template>
        
        <template v-if="activeTab === 'api'">
          <el-form-item label="API类型">
            <el-select v-model="form.apiType">
              <el-option label="REST" value="REST" />
              <el-option label="OData" value="OData" />
              <el-option label="Graph" value="Graph" />
            </el-select>
          </el-form-item>
          <el-form-item label="API地址">
            <el-input v-model="form.baseUrl" placeholder="https://api.example.com" />
          </el-form-item>
          <el-form-item label="认证类型">
            <el-select v-model="form.authType">
              <el-option label="无认证" value="NONE" />
              <el-option label="Basic认证" value="BASIC" />
              <el-option label="OAuth2" value="OAUTH2" />
            </el-select>
          </el-form-item>
        </template>
        
        <template v-if="activeTab === 'webservice'">
          <el-form-item label="WSDL地址">
            <el-input v-model="form.wsdlUrl" placeholder="https://..." />
          </el-form-item>
          <el-form-item label="服务名称">
            <el-input v-model="form.serviceName" />
          </el-form-item>
          <el-form-item label="端点地址">
            <el-input v-model="form.endpointUrl" />
          </el-form-item>
        </template>
        
        <template v-if="activeTab === 'rfc'">
          <el-form-item label="连接类型">
            <el-select v-model="form.connectionType">
              <el-option label="Direct" value="DIRECT" />
              <el-option label="JCO" value="JCO" />
            </el-select>
          </el-form-item>
          <el-form-item label="主机">
            <el-input v-model="form.host" />
          </el-form-item>
          <el-form-item label="端口">
            <el-input-number v-model="form.port" :min="1" :max="65535" />
          </el-form-item>
          <el-form-item label="客户端">
            <el-input v-model="form.client" placeholder="如: 100" />
          </el-form-item>
          <el-form-item label="系统编号">
            <el-input v-model="form.systemNumber" />
          </el-form-item>
        </template>
        
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
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { sapApi } from '@/api/sap'

const activeTab = ref('hana')
const loading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)

const tableData = ref([])
const form = reactive(getDefaultForm())

const dialogTitle = computed(() => isEdit.value ? '编辑配置' : '新增配置')

function getDefaultForm() {
  return {
    configName: '', configCode: '', status: 1,
    host: '', port: 30015, databaseName: '', username: '', password: '',
    apiType: 'REST', baseUrl: '', authType: 'NONE',
    wsdlUrl: '', serviceName: '', endpointUrl: '',
    connectionType: 'DIRECT', client: '', systemNumber: ''
  }
}

const apiMap = {
  rfc: { list: sapApi.listRfc, create: sapApi.createRfc, update: sapApi.updateRfc, delete: sapApi.deleteRfc, test: null },
  api: { list: sapApi.listApi, create: sapApi.createApi, update: sapApi.updateApi, delete: sapApi.deleteApi, test: null },
  webservice: { list: sapApi.listWebservice, create: sapApi.createWebservice, update: sapApi.updateWebservice, delete: sapApi.deleteWebservice, test: null },
  hana: { list: sapApi.listHana, create: sapApi.createHana, update: sapApi.updateHana, delete: sapApi.deleteHana, test: sapApi.testHana }
}

const fetchData = async () => {
  loading.value = true
  try {
    const api = apiMap[activeTab.value]
    const res = await api.list()
    tableData.value = res.data || []
  } catch (e) { ElMessage.error('加载失败') }
  finally { loading.value = false }
}

const handleCreate = () => {
  isEdit.value = false
  Object.assign(form, getDefaultForm())
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  Object.assign(form, { ...getDefaultForm(), ...row })
  dialogVisible.value = true
}

const handleSubmit = async () => {
  try {
    const api = apiMap[activeTab.value]
    if (isEdit.value) await api.update(form.id, form)
    else await api.create(form)
    ElMessage.success('操作成功')
    dialogVisible.value = false
    fetchData()
  } catch (e) { ElMessage.error('操作失败') }
}

const handleDelete = async (row) => {
  await ElMessageBox.confirm(`删除 "${row.configName}"?`)
  const api = apiMap[activeTab.value]
  await api.delete(row.id)
  ElMessage.success('删除成功')
  fetchData()
}

const handleTest = async (row) => {
  try {
    await sapApi.testHana(row.id)
    ElMessage.success('连接成功')
  } catch (e) { ElMessage.error('连接失败') }
}

watch(activeTab, () => fetchData())
onMounted(() => fetchData())
</script>

<style scoped>
.sap-config-page { padding: 0; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
</style>

<template>
  <div class="report-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>报表配置</span>
          <el-button type="primary" @click="handleCreate">新建报表</el-button>
        </div>
      </template>
      
      <el-row :gutter="20">
        <el-col :span="6" v-for="report in reportList" :key="report.id">
          <el-card class="report-card" shadow="hover" @click="handleView(report)">
            <div class="report-info">
              <h4>{{ report.reportName }}</h4>
              <p>{{ report.description || '暂无描述' }}</p>
              <div class="report-meta">
                <el-tag size="small">{{ report.status === 1 ? '启用' : '禁用' }}</el-tag>
                <span class="date">{{ report.createdAt }}</span>
              </div>
            </div>
            <div class="report-actions">
              <el-button link type="primary" @click.stop="handleEdit(report)">编辑</el-button>
              <el-button link type="danger" @click.stop="handleDelete(report)">删除</el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>
      
      <el-empty v-if="reportList.length === 0" description="暂无报表，点击新建按钮创建" />
    </el-card>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑报表' : '新建报表'" width="800px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="报表名称" prop="reportName">
          <el-input v-model="form.reportName" placeholder="请输入报表名称" />
        </el-form-item>
        <el-form-item label="报表编码" prop="reportCode">
          <el-input v-model="form.reportCode" placeholder="请输入报表编码" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入描述" />
        </el-form-item>
        <el-form-item label="刷新间隔">
          <el-input-number v-model="form.refreshInterval" :min="10" :max="3600" />
          <span class="ml-2">秒</span>
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-divider>组件配置</el-divider>
        
        <div v-for="(widget, index) in form.widgets" :key="index" class="widget-config">
          <el-card shadow="never">
            <template #header>
              <div class="widget-header">
                <span>组件 {{ index + 1 }}</span>
                <el-button link type="danger" @click="removeWidget(index)">删除</el-button>
              </div>
            </template>
            <el-form-item label="组件名称">
              <el-input v-model="widget.widgetName" placeholder="组件名称" />
            </el-form-item>
            <el-form-item label="图表类型">
              <el-select v-model="widget.widgetType" placeholder="选择图表类型">
                <el-option label="折线图" value="LINE" />
                <el-option label="柱状图" value="BAR" />
                <el-option label="饼图" value="PIE" />
                <el-option label="散点图" value="SCATTER" />
                <el-option label="仪表盘" value="GAUGE" />
                <el-option label="表格" value="TABLE" />
              </el-select>
            </el-form-item>
            <el-form-item label="数据源类型">
              <el-select v-model="widget.dataSourceType" placeholder="选择数据源">
                <el-option label="SQL" value="SQL" />
                <el-option label="HANA" value="HANA" />
                <el-option label="API" value="API" />
              </el-select>
            </el-form-item>
            <el-form-item v-if="widget.dataSourceType === 'SQL'" label="SQL语句">
              <el-input 
                v-model="widget.dataSourceConfig.sql" 
                type="textarea" 
                :rows="4"
                placeholder="SELECT * FROM table WHERE id = '${id}'"
              />
            </el-form-item>
            <el-form-item v-if="widget.dataSourceType === 'API'" label="API地址">
              <el-input v-model="widget.dataSourceConfig.url" placeholder="https://api.example.com/data" />
            </el-form-item>
          </el-card>
        </div>
        
        <el-button type="dashed" @click="addWidget" class="add-widget-btn">
          <el-icon><Plus /></el-icon> 添加组件
        </el-button>
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
import { Plus } from '@element-plus/icons-vue'
import { reportApi } from '@/api/report'

const reportList = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)

const form = reactive({
  reportName: '',
  reportCode: '',
  description: '',
  refreshInterval: 60,
  status: 1,
  widgets: []
})

const rules = {
  reportName: [{ required: true, message: '请输入报表名称', trigger: 'blur' }],
  reportCode: [{ required: true, message: '请输入报表编码', trigger: 'blur' }]
}

const fetchList = async () => {
  try {
    const res = await reportApi.list()
    reportList.value = res.data || []
  } catch (error) {
    ElMessage.error('加载报表列表失败')
  }
}

const handleCreate = () => {
  isEdit.value = false
  Object.assign(form, {
    reportName: '',
    reportCode: '',
    description: '',
    refreshInterval: 60,
    status: 1,
    widgets: []
  })
  dialogVisible.value = true
}

const handleEdit = (report) => {
  isEdit.value = true
  Object.assign(form, { ...report, widgets: [] })
  dialogVisible.value = true
}

const handleView = (report) => {
  ElMessage.info(`查看报表: ${report.reportName}`)
}

const handleDelete = async (report) => {
  try {
    await ElMessageBox.confirm(`确定删除报表 "${report.reportName}" 吗？`, '提示', { type: 'warning' })
    await reportApi.delete(report.id)
    ElMessage.success('删除成功')
    fetchList()
  } catch (e) {
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (isEdit.value) {
          await reportApi.update(form.id, form)
          ElMessage.success('编辑成功')
        } else {
          await reportApi.create(form)
          ElMessage.success('创建成功')
        }
        dialogVisible.value = false
        fetchList()
      } catch (error) {
        ElMessage.error(isEdit.value ? '编辑失败' : '创建失败')
      }
    }
  })
}

const addWidget = () => {
  form.widgets.push({
    widgetName: '',
    widgetType: 'LINE',
    dataSourceType: 'SQL',
    dataSourceConfig: { sql: '', url: '' },
    chartConfig: {},
    positionConfig: {},
    sortOrder: form.widgets.length
  })
}

const removeWidget = (index) => {
  form.widgets.splice(index, 1)
}

onMounted(() => {
  fetchList()
})
</script>

<style scoped>
.report-page { padding: 0; }

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.report-card {
  margin-bottom: 20px;
  cursor: pointer;
}

.report-info h4 { margin: 0 0 10px; }
.report-info p { color: #666; font-size: 13px; margin: 0 0 10px; }

.report-meta {
  display: flex;
  align-items: center;
  gap: 10px;
}

.report-meta .date { color: #999; font-size: 12px; }

.report-actions {
  margin-top: 15px;
  padding-top: 15px;
  border-top: 1px solid #f0f0f0;
}

.widget-config { margin-bottom: 15px; }

.widget-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.add-widget-btn { width: 100%; margin-top: 15px; }

.ml-2 { margin-left: 8px; }
</style>

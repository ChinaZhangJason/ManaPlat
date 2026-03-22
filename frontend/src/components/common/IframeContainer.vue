<template>
  <div class="iframe-container" :style="{ height: height }">
    <div v-if="loading" class="loading-mask">
      <div class="loading-content">
        <el-icon class="loading-icon" :size="40"><Loading /></el-icon>
        <p>正在加载页面...</p>
      </div>
    </div>
    
    <div v-else-if="error" class="error-mask">
      <div class="error-content">
        <el-icon :size="60" color="#f56c6c"><CircleCloseFilled /></el-icon>
        <h3>页面加载失败</h3>
        <p>{{ error }}</p>
        <el-button type="primary" @click="handleRefresh">
          <el-icon><Refresh /></el-icon> 重新加载
        </el-button>
      </div>
    </div>
    
    <div v-else class="iframe-wrapper">
      <div class="iframe-toolbar">
        <div class="toolbar-left">
          <el-tag size="small" type="info">
            <el-icon><Link /></el-icon>
            {{ displayUrl }}
          </el-tag>
        </div>
        <div class="toolbar-right">
          <el-tooltip content="刷新页面" placement="bottom">
            <el-button size="small" circle @click="handleRefresh">
              <el-icon><Refresh /></el-icon>
            </el-button>
          </el-tooltip>
          <el-tooltip content="在新窗口打开" placement="bottom">
            <el-button size="small" circle @click="handleOpenExternal">
              <el-icon><TopRight /></el-icon>
            </el-button>
          </el-tooltip>
          <el-tooltip :content="autoRefreshEnabled ? '停止自动刷新' : '启用自动刷新'" placement="bottom">
            <el-button 
              size="small" 
              circle 
              :type="autoRefreshEnabled ? 'primary' : 'default'"
              @click="toggleAutoRefresh"
            >
              <el-icon><Timer /></el-icon>
            </el-button>
          </el-tooltip>
        </div>
      </div>
      <iframe
        ref="iframeRef"
        :src="effectiveSrc"
        :class="['iframe-content', { 'iframe-hidden': loading || error }]"
        frameborder="0"
        scrolling="auto"
        allowfullscreen
        @load="handleLoad"
        @error="handleIframeError"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted, onUnmounted } from 'vue'
import { Loading, Refresh, CircleCloseFilled, Link, TopRight, Timer } from '@element-plus/icons-vue'

const props = defineProps({
  src: {
    type: String,
    default: ''
  },
  height: {
    type: String,
    default: '600px'
  },
  loading: {
    type: Boolean,
    default: false
  },
  error: {
    type: String,
    default: ''
  },
  autoRefreshInterval: {
    type: Number,
    default: 0
  }
})

const emit = defineEmits(['load', 'error', 'refresh'])

const iframeRef = ref(null)
const internalLoading = ref(false)
const internalError = ref('')
const refreshKey = ref(Date.now())
const autoRefreshEnabled = ref(false)
let autoRefreshTimer = null

const isLoading = computed(() => props.loading || internalLoading.value)
const isError = computed(() => props.error || internalError.value)

const effectiveSrc = computed(() => {
  if (!props.src) return ''
  const url = new URL(props.src, window.location.origin)
  url.searchParams.set('_t', refreshKey.value)
  return url.toString()
})

const displayUrl = computed(() => {
  if (!props.src) return ''
  try {
    const url = new URL(props.src)
    return url.hostname + url.pathname
  } catch {
    return props.src.substring(0, 50)
  }
})

const handleLoad = () => {
  internalLoading.value = false
  internalError.value = ''
  emit('load')
}

const handleIframeError = () => {
  internalLoading.value = false
  internalError.value = '无法加载页面，请检查网络连接或目标地址'
  emit('error', internalError.value)
}

const handleRefresh = () => {
  internalLoading.value = true
  internalError.value = ''
  refreshKey.value = Date.now()
  emit('refresh')
  
  if (iframeRef.value) {
    const iframe = iframeRef.value
    iframe.src = 'about:blank'
    setTimeout(() => {
      iframe.src = effectiveSrc.value
    }, 100)
  }
}

const handleOpenExternal = () => {
  if (props.src) {
    window.open(props.src, '_blank')
  }
}

const toggleAutoRefresh = () => {
  autoRefreshEnabled.value = !autoRefreshEnabled.value
  
  if (autoRefreshTimer) {
    clearInterval(autoRefreshTimer)
    autoRefreshTimer = null
  }
  
  if (autoRefreshEnabled.value) {
    const interval = props.autoRefreshInterval > 0 ? props.autoRefreshInterval : 30000
    autoRefreshTimer = setInterval(() => {
      handleRefresh()
    }, interval)
  }
}

watch(() => props.src, () => {
  internalLoading.value = true
  internalError.value = ''
})

onMounted(() => {
  if (props.src) {
    internalLoading.value = true
  }
})

onUnmounted(() => {
  if (autoRefreshTimer) {
    clearInterval(autoRefreshTimer)
    autoRefreshTimer = null
  }
})

defineExpose({
  refresh: handleRefresh,
  openExternal: handleOpenExternal
})
</script>

<style scoped>
.iframe-container {
  position: relative;
  width: 100%;
  min-height: 400px;
  background: #f5f7fa;
  border-radius: 8px;
  overflow: hidden;
}

.loading-mask,
.error-mask {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f7fa;
  z-index: 10;
}

.loading-content,
.error-content {
  text-align: center;
  color: #606266;
}

.loading-icon {
  animation: rotate 1s linear infinite;
  margin-bottom: 16px;
  color: #409eff;
}

@keyframes rotate {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

.error-content h3 {
  margin: 16px 0 8px;
  color: #303133;
}

.error-content p {
  margin-bottom: 20px;
  color: #909399;
}

.iframe-wrapper {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.iframe-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 12px;
  background: #fff;
  border-bottom: 1px solid #e4e7ed;
  flex-shrink: 0;
}

.toolbar-left {
  display: flex;
  align-items: center;
  overflow: hidden;
}

.toolbar-left .el-tag {
  max-width: 300px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.toolbar-right {
  display: flex;
  gap: 8px;
}

.iframe-content {
  flex: 1;
  width: 100%;
  border: none;
  background: #fff;
}

.iframe-hidden {
  visibility: hidden;
}
</style>

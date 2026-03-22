<template>
  <div 
    class="app-container" 
    :class="{ 'is-mobile': appStore.isMobile, 'is-tablet': appStore.isTablet }"
  >
    <slot></slot>
  </div>
</template>

<script setup>
import { onMounted, onUnmounted } from 'vue'
import { useAppStore } from '@/stores/app'

const appStore = useAppStore()

const handleResize = () => {
  appStore.initDevice()
}

onMounted(() => {
  appStore.loadSidebarState()
  appStore.initDevice()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
})
</script>

<style scoped>
.app-container {
  width: 100%;
  min-height: 100vh;
}

.is-mobile .main-content {
  margin-left: 0 !important;
}

.is-tablet .main-content {
  margin-left: 0 !important;
}
</style>

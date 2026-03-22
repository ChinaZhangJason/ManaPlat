<template>
  <el-container class="app-layout" :class="{ 'is-mobile': appStore.isMobile }">
    <AppSidebar 
      ref="sidebarRef"
      :menu-list="menuList"
    />

    <el-container 
      class="main-container" 
      :class="{ 'is-collapse': isCollapse, 'is-fixed': isFixed }"
      :style="mainContainerStyle"
    >
      <AppHeader
        :sidebar-width="sidebarWidth"
        @open-sidebar="handleOpenSidebar"
      />

      <el-main class="app-main">
        <router-view v-slot="{ Component, route }">
          <transition name="fade-transform" mode="out-in">
            <keep-alive :include="cachedViews">
              <component :is="Component" :key="route.path" />
            </keep-alive>
          </transition>
        </router-view>
      </el-main>
    </el-container>

    <div 
      v-if="appStore.isMobile && appStore.sidebar.opened" 
      class="mask" 
      @click="handleCloseSidebar"
    ></div>
  </el-container>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useAppStore } from '@/stores/app'
import AppSidebar from './AppSidebar.vue'
import AppHeader from './AppHeader.vue'

const appStore = useAppStore()
const route = useRoute()
const sidebarRef = ref(null)

const isCollapse = computed(() => appStore.isCollapse)
const isFixed = computed(() => appStore.isFixed)
const cachedViews = computed(() => appStore.cachedViews)

const sidebarWidth = computed(() => {
  if (appStore.isMobile) return '0px'
  if (isCollapse.value || appStore.isIconMode) return '64px'
  return '200px'
})

const mainContainerStyle = computed(() => ({
  marginLeft: sidebarWidth.value
}))

const menuList = computed(() => {
  return route.meta?.menuList || [
    { path: '/dashboard', title: '仪表盘', icon: 'Odometer' },
    { 
      path: '/monitoring', 
      title: '监控管理', 
      icon: 'Monitor',
      children: [
        { path: '/monitoring/systems', title: '系统监控', icon: 'Monitor' },
        { path: '/monitoring/report', title: '报表配置', icon: 'DataLine' },
        { path: '/monitoring/alert', title: '告警配置', icon: 'Bell' }
      ]
    },
    { 
      path: '/test', 
      title: '测试工具', 
      icon: 'Tools',
      children: [
        { path: '/test/api', title: 'API测试', icon: 'Connection' },
        { path: '/test/sql', title: 'SQL测试', icon: 'Document' }
      ]
    },
    { path: '/application', title: '应用管理', icon: 'Box' },
    { path: '/integration', title: '系统集成', icon: 'Connection' },
    { 
      path: '/system', 
      title: '系统管理', 
      icon: 'Setting',
      children: [
        { path: '/system/user', title: '用户管理', icon: 'User' },
        { path: '/system/role', title: '角色管理', icon: 'UserFilled' },
        { path: '/system/menu', title: '菜单管理', icon: 'Menu' },
        { path: '/system/permission', title: '权限管理', icon: 'Key' },
        { path: '/system/log', title: '操作日志', icon: 'Document' }
      ]
    }
  ]
})

const handleOpenSidebar = () => {
  if (sidebarRef.value) {
    sidebarRef.value.openDrawer()
  }
}

const handleCloseSidebar = () => {
  appStore.closeSidebar()
}

watch(() => route.path, () => {
  appStore.addVisitedView(route)
  if (route.name) {
    appStore.addCachedView(route)
  }
}, { immediate: true })

onMounted(() => {
  appStore.initDevice()
})
</script>

<style scoped>
.app-layout {
  min-height: 100vh;
  position: relative;
}

.main-container {
  min-height: 100vh;
  transition: margin-left 0.3s;
  background: #f0f2f5;
}

.main-container.is-fixed {
  margin-left: 0 !important;
}

.app-main {
  padding: 20px;
  min-height: calc(100vh - 60px);
}

.mask {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  z-index: 1000;
}

.fade-transform-leave-active,
.fade-transform-enter-active {
  transition: all 0.3s;
}

.fade-transform-enter-from {
  opacity: 0;
  transform: translateX(-20px);
}

.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(20px);
}
</style>

<template>
  <div 
    class="app-sidebar" 
    :class="{ 
      'is-collapse': isCollapse, 
      'is-fixed': isFixed,
      'is-icon-mode': isIconMode,
      'is-mobile': appStore.isMobile,
      'is-open': !isCollapse && !appStore.isMobile
    }"
  >
    <div class="sidebar-logo">
      <router-link to="/">
        <img v-if="isIconMode" src="@/assets/logo-icon.png" class="logo-icon" />
        <span v-else class="logo-text">SAP管理平台</span>
      </router-link>
    </div>

    <el-scrollbar class="sidebar-scroll">
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse && !isFixed"
        :collapse-transition="false"
        :router="true"
        :unique-opened="true"
        class="sidebar-menu"
      >
        <template v-for="item in menuList" :key="item.path">
          <el-menu-item 
            v-if="!item.children || item.children.length === 0"
            :index="item.path"
          >
            <el-icon><component :is="item.icon" /></el-icon>
            <template #title>{{ item.title }}</template>
          </el-menu-item>

          <el-sub-menu v-else :index="item.path">
            <template #title>
              <el-icon><component :is="item.icon" /></el-icon>
              <span>{{ item.title }}</span>
            </template>
            <el-menu-item 
              v-for="child in item.children" 
              :key="child.path"
              :index="child.path"
            >
              <el-icon><component :is="child.icon" /></component></el-icon>
              <template #title>{{ child.title }}</template>
            </el-menu-item>
          </el-sub-menu>
        </template>
      </el-menu>
    </el-scrollbar>

    <div class="sidebar-footer">
      <div class="collapse-btn" @click="handleCollapse" title="折叠/展开">
        <el-icon><Expand v-if="isCollapse" /><Fold v-else /></el-icon>
      </div>
      <div class="icon-mode-btn" @click="handleIconMode" title="图标模式">
        <el-icon><Grid v-if="isIconMode" /><Menu v-else /></el-icon>
      </div>
      <div class="fixed-btn" @click="handleFixed" title="固定/浮动">
        <el-icon><Lock v-if="isFixed" /><Unlock v-else /></el-icon>
      </div>
    </div>

    <el-drawer
      v-if="appStore.isMobile"
      v-model="drawerVisible"
      direction="ltr"
      :with-header="false"
      size="240px"
      class="mobile-drawer"
    >
      <div class="sidebar-logo">
        <span class="logo-text">SAP管理平台</span>
      </div>
      <el-scrollbar class="sidebar-scroll">
        <el-menu
          :default-active="activeMenu"
          :router="true"
          :unique-opened="true"
          class="sidebar-menu"
        >
          <template v-for="item in menuList" :key="item.path">
            <el-menu-item 
              v-if="!item.children || item.children.length === 0"
              :index="item.path"
              @click="drawerVisible = false"
            >
              <el-icon><component :is="item.icon" /></el-icon>
              <template #title>{{ item.title }}</template>
            </el-menu-item>

            <el-sub-menu v-else :index="item.path">
              <template #title>
                <el-icon><component :is="item.icon" /></el-icon>
                <span>{{ item.title }}</span>
              </template>
              <el-menu-item 
                v-for="child in item.children" 
                :key="child.path"
                :index="child.path"
                @click="drawerVisible = false"
              >
                <el-icon><component :is="child.icon" /></component></el-icon>
                <template #title>{{ child.title }}</template>
              </el-menu-item>
            </el-sub-menu>
          </template>
        </el-menu>
      </el-scrollbar>
    </el-drawer>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { useRoute } from 'vue-router'
import { useAppStore } from '@/stores/app'
import { Fold, Expand, Grid, Menu, Lock, Unlock } from '@element-plus/icons-vue'

const props = defineProps({
  menuList: {
    type: Array,
    default: () => []
  }
})

const appStore = useAppStore()
const route = useRoute()
const drawerVisible = ref(false)

const isCollapse = computed(() => appStore.isCollapse)
const isFixed = computed(() => appStore.isFixed)
const isIconMode = computed(() => appStore.isIconMode)

const activeMenu = computed(() => route.path)

watch(() => appStore.isMobile, (mobile) => {
  if (mobile) {
    drawerVisible.value = false
  }
})

const handleCollapse = () => {
  appStore.toggleSidebar()
}

const handleIconMode = () => {
  appStore.toggleIconMode()
}

const handleFixed = () => {
  appStore.toggleFixed()
}

const openDrawer = () => {
  if (appStore.isMobile) {
    drawerVisible.value = true
  }
}

defineExpose({ openDrawer })
</script>

<style scoped>
.app-sidebar {
  position: fixed;
  top: 0;
  left: 0;
  bottom: 0;
  width: 200px;
  background: #304156;
  transition: width 0.3s, transform 0.3s;
  z-index: 1001;
  display: flex;
  flex-direction: column;
}

.app-sidebar.is-collapse {
  width: 64px;
}

.app-sidebar.is-icon-mode {
  width: 64px;
}

.app-sidebar.is-fixed {
  position: fixed;
}

.app-sidebar:not(.is-fixed):not(.is-open) {
  transform: translateX(-100%);
}

.app-sidebar.is-mobile {
  width: 100%;
  z-index: 2001;
}

.sidebar-logo {
  height: 60px;
  line-height: 60px;
  text-align: center;
  background: #263445;
  overflow: hidden;
}

.logo-text {
  color: #fff;
  font-size: 18px;
  font-weight: bold;
}

.logo-icon {
  width: 32px;
  height: 32px;
}

.sidebar-scroll {
  flex: 1;
  overflow-y: auto;
}

.sidebar-menu {
  border-right: none;
  background: transparent;
}

:deep(.el-menu) {
  background: transparent;
}

:deep(.el-menu-item),
:deep(.el-sub-menu__title) {
  color: #bfcbd9;
  height: 50px;
  line-height: 50px;
}

:deep(.el-menu-item:hover),
:deep(.el-sub-menu__title:hover) {
  background: #263445 !important;
  color: #409eff;
}

:deep(.el-menu-item.is-active) {
  background: #263445 !important;
  color: #409eff;
}

:deep(.el-sub-menu .el-menu-item) {
  height: 45px;
  line-height: 45px;
  padding-left: 50px !important;
}

.sidebar-footer {
  height: 50px;
  border-top: 1px solid #263445;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 5px;
  background: #263445;
}

.collapse-btn,
.icon-mode-btn,
.fixed-btn {
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #bfcbd9;
  cursor: pointer;
  border-radius: 4px;
  transition: all 0.3s;
}

.collapse-btn:hover,
.icon-mode-btn:hover,
.fixed-btn:hover {
  background: #409eff;
  color: #fff;
}

.is-collapse .sidebar-logo {
  display: flex;
  align-items: center;
  justify-content: center;
}

.is-collapse .sidebar-footer {
  flex-direction: column;
  height: auto;
  padding: 5px 0;
  gap: 2px;
}

.mobile-drawer {
  --el-drawer-bg-color: #304156;
}
</style>

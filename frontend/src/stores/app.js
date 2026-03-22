import { defineStore } from 'pinia'

export const useAppStore = defineStore('app', {
  state: () => ({
    sidebar: {
      opened: true,
      fixed: true,
      withoutAnimation: false,
      isIcon: false
    },
    device: 'desktop',
    size: 'default',
    cachedViews: [],
    visitedViews: []
  }),

  getters: {
    isCollapse: (state) => !state.sidebar.opened,
    isFixed: (state) => state.sidebar.fixed,
    isIconMode: (state) => state.sidebar.isIcon,
    isMobile: (state) => state.device === 'mobile',
    isDesktop: (state) => state.device === 'desktop'
  },

  actions: {
    toggleSidebar() {
      this.sidebar.opened = !this.sidebar.opened
      this.sidebar.withoutAnimation = false
      this.saveSidebarState()
    },

    closeSidebar(withAnimation = true) {
      this.sidebar.opened = false
      this.sidebar.withoutAnimation = withAnimation
      this.saveSidebarState()
    },

    toggleDevice(device) {
      this.device = device
    },

    toggleFixed() {
      this.sidebar.fixed = !this.sidebar.fixed
      this.saveSidebarState()
    },

    toggleIconMode() {
      this.sidebar.isIcon = !this.sidebar.isIcon
      this.saveSidebarState()
    },

    saveSidebarState() {
      localStorage.setItem('sidebar', JSON.stringify({
        opened: this.sidebar.opened,
        fixed: this.sidebar.fixed,
        isIcon: this.sidebar.isIcon
      }))
    },

    loadSidebarState() {
      const saved = localStorage.getItem('sidebar')
      if (saved) {
        const state = JSON.parse(saved)
        this.sidebar.opened = state.opened !== false
        this.sidebar.fixed = state.fixed !== false
        this.sidebar.isIcon = state.isIcon || false
      }
    },

    addCachedView(view) {
      if (this.cachedViews.includes(view.name)) return
      this.cachedViews.push(view.name)
    },

    removeCachedView(view) {
      const index = this.cachedViews.indexOf(view.name)
      index > -1 && this.cachedViews.splice(index, 1)
    },

    addVisitedView(view) {
      if (this.visitedViews.some(v => v.path === view.path)) return
      this.visitedViews.push({
        path: view.path,
        name: view.name,
        meta: { ...view.meta }
      })
    },

    removeVisitedView(view) {
      const index = this.visitedViews.findIndex(v => v.path === view.path)
      this.visitedViews.splice(index, 1)
    },

    initDevice() {
      const width = window.innerWidth
      if (width < 768) {
        this.device = 'mobile'
        this.sidebar.opened = false
      } else if (width < 1200) {
        this.device = 'tablet'
      } else {
        this.device = 'desktop'
      }
    }
  }
})

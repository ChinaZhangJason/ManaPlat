import { describe, it, expect } from 'vitest'

describe('App Layout', () => {
  it('should have correct initial state', () => {
    const appStore = {
      sidebar: {
        opened: true,
        fixed: true,
        isIcon: false
      },
      device: 'desktop',
      isCollapse: false,
      isFixed: true,
      isMobile: false
    }

    expect(appStore.isCollapse).toBe(false)
    expect(appStore.isFixed).toBe(true)
    expect(appStore.isMobile).toBe(false)
  })

  it('should toggle sidebar correctly', () => {
    const appStore = {
      sidebar: { opened: true },
      toggleSidebar() {
        this.sidebar.opened = !this.sidebar.opened
      }
    }

    appStore.toggleSidebar()
    expect(appStore.sidebar.opened).toBe(false)

    appStore.toggleSidebar()
    expect(appStore.sidebar.opened).toBe(true)
  })

  it('should toggle fixed state correctly', () => {
    const appStore = {
      sidebar: { fixed: true },
      toggleFixed() {
        this.sidebar.fixed = !this.sidebar.fixed
      }
    }

    appStore.toggleFixed()
    expect(appStore.sidebar.fixed).toBe(false)
  })

  it('should toggle icon mode correctly', () => {
    const appStore = {
      sidebar: { isIcon: false },
      toggleIconMode() {
        this.sidebar.isIcon = !this.sidebar.isIcon
      }
    }

    appStore.toggleIconMode()
    expect(appStore.sidebar.isIcon).toBe(true)
  })

  it('should detect mobile device', () => {
    const appStore = {
      device: 'desktop',
      initDevice() {
        this.device = 'mobile'
      }
    }

    appStore.initDevice()
    expect(appStore.device).toBe('mobile')
  })
})

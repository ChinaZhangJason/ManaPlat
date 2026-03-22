import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/auth/Login.vue'),
    meta: { requiresAuth: false, title: '登录' }
  },
  {
    path: '/reset-password',
    name: 'PasswordReset',
    component: () => import('@/views/auth/PasswordReset.vue'),
    meta: { requiresAuth: false, title: '密码重置' }
  },
  {
    path: '/',
    component: () => import('@/views/Layout.vue'),
    redirect: '/dashboard',
    meta: { requiresAuth: true },
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/Index.vue'),
        meta: { 
          title: '仪表盘', 
          icon: 'Odometer',
          menuCode: 'dashboard'
        }
      },
      {
        path: 'monitoring',
        name: 'Monitoring',
        redirect: '/monitoring/systems',
        meta: { title: '监控管理', icon: 'Monitor', menuCode: 'monitor' },
        children: [
          {
            path: 'systems',
            name: 'MonitoringSystems',
            component: () => import('@/views/monitoring/Index.vue'),
            meta: { title: '系统监控', icon: 'Monitor', menuCode: 'monitor_system' }
          },
          {
            path: 'report',
            name: 'MonitoringReport',
            component: () => import('@/views/monitoring/Report.vue'),
            meta: { title: '报表配置', icon: 'DataLine', menuCode: 'monitor_report' }
          },
          {
            path: 'alert',
            name: 'MonitoringAlert',
            component: () => import('@/views/monitoring/Alert.vue'),
            meta: { title: '告警配置', icon: 'Bell', menuCode: 'monitor_alert' }
          }
        ]
      },
      {
        path: 'test',
        name: 'Test',
        redirect: '/test/api',
        meta: { title: '测试工具', icon: 'Tools', menuCode: 'test' },
        children: [
          {
            path: 'api',
            name: 'TestApi',
            component: () => import('@/views/test/ApiTest.vue'),
            meta: { title: 'API测试', icon: 'Connection', menuCode: 'test_api' }
          },
          {
            path: 'sql',
            name: 'TestSql',
            component: () => import('@/views/test/SqlTest.vue'),
            meta: { title: 'SQL测试', icon: 'Document', menuCode: 'test_sql' }
          }
        ]
      },
      {
        path: 'application',
        name: 'Application',
        redirect: '/application/systems',
        meta: { title: '应用管理', icon: 'Box', menuCode: 'application' },
        children: [
          {
            path: 'systems',
            name: 'ApplicationSystems',
            component: () => import('@/views/application/Index.vue'),
            meta: { title: '系统配置', icon: 'Box', menuCode: 'application_systems' }
          },
          {
            path: 'sap-config',
            name: 'SapConfig',
            component: () => import('@/views/application/SapConfig.vue'),
            meta: { title: 'SAP连接配置', icon: 'Connection', menuCode: 'application_sap' }
          }
        ]
      },
      {
        path: 'integration',
        name: 'Integration',
        component: () => import('@/views/integration/Index.vue'),
        meta: { title: '系统集成', icon: 'Connection', menuCode: 'integrate' }
      },
      {
        path: 'system',
        name: 'System',
        redirect: '/system/user',
        meta: { title: '系统管理', icon: 'Setting', menuCode: 'system' },
        children: [
          {
            path: 'user',
            name: 'SystemUser',
            component: () => import('@/views/system/User.vue'),
            meta: { title: '用户管理', icon: 'User', menuCode: 'system_user' }
          },
          {
            path: 'role',
            name: 'SystemRole',
            component: () => import('@/views/system/Role.vue'),
            meta: { title: '角色管理', icon: 'UserFilled', menuCode: 'system_role' }
          },
          {
            path: 'menu',
            name: 'SystemMenu',
            component: () => import('@/views/system/Menu.vue'),
            meta: { title: '菜单管理', icon: 'Menu', menuCode: 'system_menu' }
          },
          {
            path: 'permission',
            name: 'SystemPermission',
            component: () => import('@/views/system/Permission.vue'),
            meta: { title: '权限管理', icon: 'Key', menuCode: 'system_permission' }
          },
          {
            path: 'log',
            name: 'SystemLog',
            component: () => import('@/views/system/Log.vue'),
            meta: { title: '操作日志', icon: 'Document', menuCode: 'system_log' }
          }
        ]
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('@/views/Profile.vue'),
        meta: { title: '个人中心', icon: 'User', requiresAuth: true }
      },
      {
        path: 'settings',
        name: 'Settings',
        component: () => import('@/views/Settings.vue'),
        meta: { title: '系统设置', icon: 'Setting', requiresAuth: true }
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/NotFound.vue'),
    meta: { requiresAuth: false, title: '404' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach(async (to, from, next) => {
  const userStore = useUserStore()
  
  document.title = to.meta?.title ? `${to.meta.title} - SAP管理平台` : 'SAP管理平台'
  
  if (to.meta.requiresAuth !== false && !userStore.isLoggedIn) {
    const token = localStorage.getItem('token')
    if (token) {
      try {
        await userStore.fetchUserInfo()
        next()
      } catch (error) {
        localStorage.removeItem('token')
        next('/login')
      }
    } else {
      next('/login')
    }
  } else if (to.path === '/login' && userStore.isLoggedIn) {
    next('/dashboard')
  } else {
    next()
  }
})

export const generateTitle = (title) => {
  return title
}

export const hasPermission = (permission) => {
  const userStore = useUserStore()
  return userStore.permissions?.includes(permission) || false
}

export const hasRole = (role) => {
  const userStore = useUserStore()
  return userStore.roles?.includes(role) || false
}

export const filterAsyncRoutes = (routes, roles) => {
  const res = []
  routes.forEach(route => {
    const tmp = { ...route }
    if (hasPermission(route.meta?.permission)) {
      if (tmp.children) {
        tmp.children = filterAsyncRoutes(tmp.children, roles)
      }
      res.push(tmp)
    }
  })
  return res
}

export default router

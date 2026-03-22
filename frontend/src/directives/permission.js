import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'

const checkPermission = (el, binding) => {
  const userStore = useUserStore()
  const { value } = binding
  
  if (value && Array.isArray(value)) {
    const hasPermission = value.some(permission => {
      return userStore.roles?.includes(permission) || 
             userStore.permissions?.includes(permission)
    })
    
    if (!hasPermission) {
      el.parentNode?.removeChild(el)
    }
  } else {
    console.warn('v-permission 指令需要传入权限数组')
  }
}

export const permissionDirective = {
  mounted(el, binding) {
    checkPermission(el, binding)
  },
  updated(el, binding) {
    checkPermission(el, binding)
  }
}

export const hasPermission = (permissions) => {
  const userStore = useUserStore()
  
  if (!permissions || permissions.length === 0) {
    return true
  }
  
  return permissions.some(permission => {
    return userStore.roles?.includes(permission) || 
           userStore.permissions?.includes(permission)
  })
}

export const hasRole = (roles) => {
  const userStore = useUserStore()
  
  if (!roles || roles.length === 0) {
    return true
  }
  
  return roles.some(role => {
    return userStore.roles?.includes(role)
  })
}

export default {
  install(app) {
    app.directive('permission', permissionDirective)
  }
}

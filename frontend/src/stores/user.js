import { defineStore } from 'pinia'
import { authApi } from '@/api/auth'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    userInfo: null,
    permissions: [],
    roles: []
  }),
  
  getters: {
    isLoggedIn: (state) => !!state.token,
    username: (state) => state.userInfo?.username || '',
    userId: (state) => state.userInfo?.id || null,
    roleNames: (state) => state.roles?.map(r => r.roleCode) || [],
    isAdmin: (state) => state.roles?.some(r => r.roleCode === 'SUPER_ADMIN' || r.roleCode === 'ADMIN')
  },
  
  actions: {
    async login(username, password) {
      try {
        const res = await authApi.login(username, password)
        this.token = res.data?.token || res.token
        this.userInfo = res.data?.userInfo || res.userInfo
        this.roles = res.data?.roles || []
        this.permissions = this.extractPermissions()
        localStorage.setItem('token', this.token)
        return res
      } catch (error) {
        throw error
      }
    },
    
    async logout() {
      try {
        await authApi.logout()
      } catch (e) {
      } finally {
        this.token = ''
        this.userInfo = null
        this.roles = []
        this.permissions = []
        localStorage.removeItem('token')
      }
    },
    
    async fetchUserInfo() {
      try {
        const res = await authApi.getUserInfo()
        this.userInfo = res.data || res
        this.roles = res.data?.roles || []
        this.permissions = this.extractPermissions()
        return this.userInfo
      } catch (error) {
        throw error
      }
    },
    
    extractPermissions() {
      if (!this.roles || this.roles.length === 0) return []
      const permissions = []
      this.roles.forEach(role => {
        if (role.permissions) {
          permissions.push(...role.permissions.map(p => p.permissionCode))
        }
      })
      return [...new Set(permissions)]
    },
    
    hasPermission(permission) {
      if (this.isAdmin) return true
      return this.permissions.includes(permission)
    },
    
    hasRole(role) {
      return this.roles?.some(r => r.roleCode === role)
    }
  }
})

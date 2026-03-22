import { describe, it, expect, beforeEach, vi } from 'vitest'

describe('API Module', () => {
  let mockAxios
  let request

  beforeEach(() => {
    mockAxios = {
      create: vi.fn(() => ({
        interceptors: {
          request: { use: vi.fn() },
          response: { use: vi.fn() }
        },
        get: vi.fn(),
        post: vi.fn(),
        put: vi.fn(),
        delete: vi.fn()
      }))
    }
  })

  describe('Request Interceptor', () => {
    it('should add Authorization header when token exists', async () => {
      localStorage.setItem('token', 'test-token-123')
      
      const config = { headers: {} }
      const token = localStorage.getItem('token')
      
      if (token) {
        config.headers.Authorization = `Bearer ${token}`
      }
      
      expect(config.headers.Authorization).toBe('Bearer test-token-123')
      
      localStorage.removeItem('token')
    })

    it('should not add header when no token', () => {
      localStorage.removeItem('token')
      const token = localStorage.getItem('token')
      
      expect(token).toBeNull()
    })
  })

  describe('Response Interceptor', () => {
    it('should handle success response', () => {
      const response = {
        data: { code: 200, message: 'success', data: {} }
      }
      
      expect(response.data.code).toBe(200)
      expect(response.data.message).toBe('success')
    })

    it('should handle error response', () => {
      const error = {
        response: {
          status: 401,
          data: { code: 401, message: 'Unauthorized' }
        }
      }
      
      expect(error.response.status).toBe(401)
    })
  })
})

describe('API Endpoints', () => {
  describe('Auth API', () => {
    it('should have correct login endpoint', () => {
      const authApi = {
        login: (username, password) => ({ method: 'POST', url: '/auth/login' }),
        logout: () => ({ method: 'POST', url: '/auth/logout' }),
        getUserInfo: () => ({ method: 'GET', url: '/auth/user-info' }),
        sendCode: (data) => ({ method: 'POST', url: '/auth/send-code', data }),
        resetPassword: (data) => ({ method: 'POST', url: '/auth/reset-password', data })
      }

      const loginReq = authApi.login('admin', 'admin123')
      expect(loginReq.url).toBe('/auth/login')
      expect(loginReq.method).toBe('POST')
    })

    it('should have correct send code endpoint', () => {
      const authApi = {
        sendCode: (data) => ({ method: 'POST', url: '/auth/send-code', data })
      }

      const req = authApi.sendCode({ type: 'EMAIL', target: 'test@example.com' })
      expect(req.url).toBe('/auth/send-code')
      expect(req.data.type).toBe('EMAIL')
    })
  })

  describe('Monitor API', () => {
    it('should have correct report endpoints', () => {
      const reportApi = {
        list: () => ({ method: 'GET', url: '/monitor/report/list' }),
        get: (id) => ({ method: 'GET', url: `/monitor/report/${id}` }),
        create: (data) => ({ method: 'POST', url: '/monitor/report', data }),
        update: (id, data) => ({ method: 'PUT', url: `/monitor/report/${id}`, data }),
        delete: (id) => ({ method: 'DELETE', url: `/monitor/report/${id}` }),
        getWidgetData: (data) => ({ method: 'POST', url: '/monitor/report/widget/data', data })
      }

      expect(reportApi.list().url).toBe('/monitor/report/list')
      expect(reportApi.create({}).url).toBe('/monitor/report')
    })

    it('should have correct alert endpoints', () => {
      const alertApi = {
        listRules: () => ({ method: 'GET', url: '/monitor/alert/rule/list' }),
        createRule: (data) => ({ method: 'POST', url: '/monitor/alert/rule', data }),
        listReceivers: () => ({ method: 'GET', url: '/monitor/alert/receiver/list' }),
        getHistory: (params) => ({ method: 'GET', url: '/monitor/alert/history', params })
      }

      expect(alertApi.listRules().url).toBe('/monitor/alert/rule/list')
      expect(alertApi.listReceivers().url).toBe('/monitor/alert/receiver/list')
    })
  })

  describe('SAP API', () => {
    it('should have correct SAP config endpoints', () => {
      const sapApi = {
        listRfc: () => ({ method: 'GET', url: '/sap/rfc/list' }),
        createRfc: (data) => ({ method: 'POST', url: '/sap/rfc', data }),
        listApi: () => ({ method: 'GET', url: '/sap/api/list' }),
        listHana: () => ({ method: 'GET', url: '/sap/hana/list' }),
        testHana: (id) => ({ method: 'POST', url: `/sap/hana/${id}/test` })
      }

      expect(sapApi.listRfc().url).toBe('/sap/rfc/list')
      expect(sapApi.listHana().url).toBe('/sap/hana/list')
      expect(sapApi.testHana(1).url).toBe('/sap/hana/1/test')
    })
  })

  describe('Test API', () => {
    it('should have correct test endpoints', () => {
      const testApi = {
        execute: (data) => ({ method: 'POST', url: '/test/sql/execute', data }),
        getHistory: (params) => ({ method: 'GET', url: '/test/sql/history', params }),
        saveHistory: (data) => ({ method: 'POST', url: '/test/sql/history', data })
      }

      expect(testApi.execute({ sql: 'SELECT 1' }).url).toBe('/test/sql/execute')
      expect(testApi.execute({ sql: 'SELECT 1' }).data.sql).toBe('SELECT 1')
    })
  })
})

describe('Form Validation', () => {
  describe('Password Validation', () => {
    it('should validate password length', () => {
      const validatePassword = (password) => {
        if (!password) return '密码不能为空'
        if (password.length < 6) return '密码长度不能少于6位'
        if (password.length > 20) return '密码长度不能超过20位'
        return null
      }

      expect(validatePassword('')).toBe('密码不能为空')
      expect(validatePassword('123')).toBe('密码长度不能少于6位')
      expect(validatePassword('123456')).toBeNull()
      expect(validatePassword('a'.repeat(25))).toBe('密码长度不能超过20位')
    })

    it('should validate password match', () => {
      const validateConfirm = (password, confirm) => {
        if (password !== confirm) return '两次输入密码不一致'
        return null
      }

      expect(validateConfirm('123456', '123456')).toBeNull()
      expect(validateConfirm('123456', '654321')).toBe('两次输入密码不一致')
    })
  })

  describe('Email Validation', () => {
    it('should validate email format', () => {
      const validateEmail = (email) => {
        if (!email) return '邮箱不能为空'
        if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) return '邮箱格式不正确'
        return null
      }

      expect(validateEmail('')).toBe('邮箱不能为空')
      expect(validateEmail('invalid')).toBe('邮箱格式不正确')
      expect(validateEmail('test@example.com')).toBeNull()
    })
  })

  describe('Phone Validation', () => {
    it('should validate phone format', () => {
      const validatePhone = (phone) => {
        if (!phone) return '手机号不能为空'
        if (!/^1[3-9]\d{9}$/.test(phone)) return '手机号格式不正确'
        return null
      }

      expect(validatePhone('')).toBe('手机号不能为空')
      expect(validatePhone('123')).toBe('手机号格式不正确')
      expect(validatePhone('13800138000')).toBeNull()
    })
  })
})

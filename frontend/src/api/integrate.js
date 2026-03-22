import request from './index'

export const integrateApi = {
  // 获取平台列表
  getPlatforms() {
    return request.get('/integrate/platforms')
  },
  
  // 获取平台详情
  getPlatform(id) {
    return request.get(`/integrate/platforms/${id}`)
  },
  
  // 获取平台by code
  getPlatformByCode(code) {
    return request.get(`/integrate/platforms/code/${code}`)
  },
  
  // 创建平台
  createPlatform(data) {
    return request.post('/integrate/platforms', data)
  },
  
  // 更新平台
  updatePlatform(id, data) {
    return request.put(`/integrate/platforms/${id}`, data)
  },
  
  // 删除平台
  deletePlatform(id) {
    return request.delete(`/integrate/platforms/${id}`)
  },
  
  // 生成SSO Token
  generateSsoToken(platformCode) {
    return request.post('/integrate/sso-token', { platformCode })
  },
  
  // 获取iframe URL
  getIframeUrl(platformCode) {
    return request.post('/integrate/iframe-url', { platformCode })
  }
}

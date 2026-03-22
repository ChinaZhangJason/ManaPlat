import request from './index'

export const applicationApi = {
  // 获取应用列表
  getList(params) {
    return request.get('/application', { params })
  },
  
  // 获取应用详情
  get(id) {
    return request.get(`/application/${id}`)
  },
  
  // 创建应用
  create(data) {
    return request.post('/application', data)
  },
  
  // 更新应用
  update(id, data) {
    return request.put(`/application/${id}`, data)
  },
  
  // 删除应用
  delete(id) {
    return request.delete(`/application/${id}`)
  },
  
  // 执行自定义操作
  executeAction(id, actionCode, params) {
    return request.post(`/application/${id}/action/${actionCode}`, params)
  }
}

import request from './index'

export const monitorApi = {
  // 获取SAP系统列表
  getSystems() {
    return request.get('/monitor/systems')
  },
  
  // 获取系统详情
  getSystem(id) {
    return request.get(`/monitor/systems/${id}`)
  },
  
  // 获取监控指标
  getMetrics(params) {
    return request.get('/monitor/metrics', { params })
  },
  
  // 获取SQL配置列表
  getSqlConfigs() {
    return request.get('/monitor/sql-config')
  },
  
  // 执行SQL预览
  executeSql(params) {
    return request.post('/monitor/sql-execute', params)
  },
  
  // 创建SQL配置
  createSqlConfig(data) {
    return request.post('/monitor/sql-config', data)
  },
  
  // 更新SQL配置
  updateSqlConfig(id, data) {
    return request.put(`/monitor/sql-config/${id}`, data)
  },
  
  // 删除SQL配置
  deleteSqlConfig(id) {
    return request.delete(`/monitor/sql-config/${id}`)
  }
}

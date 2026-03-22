import request from './index'

export const reportApi = {
  list: () => request.get('/monitor/report/list'),
  
  get: (id) => request.get(`/monitor/report/${id}`),
  
  create: (data) => request.post('/monitor/report', data),
  
  update: (id, data) => request.put(`/monitor/report/${id}`, data),
  
  delete: (id) => request.delete(`/monitor/report/${id}`),
  
  getWidgetData: (data) => request.post('/monitor/report/widget/data', data),
  
  testConnection: (data) => request.post('/monitor/report/test/connection', data)
}

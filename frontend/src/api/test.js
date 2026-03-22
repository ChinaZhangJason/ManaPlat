import request from './index'

export const testApi = {
  execute: (data) => request.post('/test/sql/execute', data),
  
  getHistory: (params) => request.get('/test/sql/history', { params }),
  
  saveHistory: (data) => request.post('/test/sql/history', data)
}

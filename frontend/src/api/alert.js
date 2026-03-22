import request from './index'

export const alertApi = {
  listRules: () => request.get('/monitor/alert/rule/list'),
  getRule: (id) => request.get(`/monitor/alert/rule/${id}`),
  createRule: (data) => request.post('/monitor/alert/rule', data),
  updateRule: (data) => request.put(`/monitor/alert/rule/${data.id}`, data),
  deleteRule: (id) => request.delete(`/monitor/alert/rule/${id}`),

  listReceivers: () => request.get('/monitor/alert/receiver/list'),
  getReceiver: (id) => request.get(`/monitor/alert/receiver/${id}`),
  createReceiver: (data) => request.post('/monitor/alert/receiver', data),
  updateReceiver: (data) => request.put(`/monitor/alert/receiver/${data.id}`, data),
  deleteReceiver: (id) => request.delete(`/monitor/alert/receiver/${id}`),

  getHistory: (params) => request.get('/monitor/alert/history', { params }),
  acknowledge: (id, userId) => request.post(`/monitor/alert/history/${id}/ack?userId=${userId}`),
  close: (id) => request.post(`/monitor/alert/history/${id}/close`)
}

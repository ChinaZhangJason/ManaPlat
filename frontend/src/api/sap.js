import request from './index'

export const sapApi = {
  listRfc: () => request.get('/sap/rfc/list'),
  createRfc: (data) => request.post('/sap/rfc', data),
  updateRfc: (id, data) => request.put(`/sap/rfc/${id}`, data),
  deleteRfc: (id) => request.delete(`/sap/rfc/${id}`),

  listApi: () => request.get('/sap/api/list'),
  createApi: (data) => request.post('/sap/api', data),
  updateApi: (id, data) => request.put(`/sap/api/${id}`, data),
  deleteApi: (id) => request.delete(`/sap/api/${id}`),

  listWebservice: () => request.get('/sap/webservice/list'),
  createWebservice: (data) => request.post('/sap/webservice', data),
  updateWebservice: (id, data) => request.put(`/sap/webservice/${id}`, data),
  deleteWebservice: (id) => request.delete(`/sap/webservice/${id}`),

  listHana: () => request.get('/sap/hana/list'),
  createHana: (data) => request.post('/sap/hana', data),
  updateHana: (id, data) => request.put(`/sap/hana/${id}`, data),
  deleteHana: (id) => request.delete(`/sap/hana/${id}`),
  testHana: (id) => request.post(`/sap/hana/${id}/test`)
}

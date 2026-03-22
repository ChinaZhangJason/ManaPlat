import request from './index'

export const authApi = {
  login(username, password) {
    return request.post('/auth/login', { username, password })
  },
  
  logout() {
    return request.post('/auth/logout')
  },
  
  getUserInfo() {
    return request.get('/auth/userinfo')
  },
  
  refreshToken() {
    return request.post('/auth/refresh')
  }
}

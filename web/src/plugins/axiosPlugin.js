import axios from 'axios'

const TOKEN_KEY = 'app-token'

axios.defaults.baseURL = process.env.API
axios.defaults.headers = {
  'Content-Type': 'application/json'
}

axios.interceptors.request.use(request => {
  const raw = localStorage.getItem(TOKEN_KEY)
  if (raw) {
    try {
      const tokenData = JSON.parse(raw)
      if (tokenData && tokenData.token) {
        request.headers.Authorization = `${tokenData.token_type || 'Bearer'} ${tokenData.token}`
      }
    } catch (e) {}
  }
  return request
})

axios.interceptors.response.use(
  response => response,
  error => {
    if (error.response && error.response.status === 401) {
      const Vue = require('vue')
      Vue.default.$toast && Vue.default.$toast.error('Phiên đăng nhập hết hạn, vui lòng đăng nhập lại.')
    }
    return Promise.reject(error.response || error)
  }
)

export default axios

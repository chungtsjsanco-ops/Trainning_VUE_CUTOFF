export default {
  login: (axios, data) => axios.post('/api/auth/login', data)
}

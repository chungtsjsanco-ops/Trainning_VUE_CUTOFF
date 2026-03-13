import axios from 'axios'
import token from '../plugins/token'

const BASE_URL = process.env.API

export default {
  async get(url, params) {
    const instance = axios.create({ baseURL: BASE_URL })
    instance.interceptors.request.use(request => {
      request.headers = {
        'Content-Type': 'application/json',
        Authorization: token.getAuthorization()
      }
      return request
    })
    const r = await instance.get(url, { params })
    return r.data
  },

  async post(url, data) {
    const instance = axios.create({ baseURL: BASE_URL })
    instance.interceptors.request.use(request => {
      request.headers = {
        'Content-Type': 'application/json',
        Authorization: token.getAuthorization()
      }
      return request
    })
    const r = await instance.post(url, data)
    return r.data
  },

  async getWithOutToken(url, params) {
    const instance = axios.create({ baseURL: BASE_URL })
    const r = await instance.get(url, { params })
    return r.data
  },

  async postWithOutToken(url, data) {
    const instance = axios.create({ baseURL: BASE_URL })
    const r = await instance.post(url, data)
    return r.data
  }
}

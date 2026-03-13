import token from './token'

const AuthPlugin = {
  authenticate(tokenData) {
    token.setToken(tokenData)
  },
  isAuthenticated() {
    return token.getToken() !== null
  },
  destroy() {
    token.destroy()
    localStorage.clear()
  },
  getToken() {
    return token.getToken()
  },
  getUserName() {
    return token.getUserName()
  },
  getRoles() {
    return token.getRoles()
  },
  getProperty(name) {
    return token.getProperty(name)
  }
}

export default function(Vue) {
  Vue.auth = AuthPlugin
  Object.defineProperties(Vue.prototype, {
    $auth: {
      get() {
        return Vue.auth
      }
    }
  })
}
